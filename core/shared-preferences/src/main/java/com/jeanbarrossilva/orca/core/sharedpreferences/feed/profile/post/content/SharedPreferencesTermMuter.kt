/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.core.sharedpreferences.feed.profile.post.content

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.jeanbarrossilva.orca.core.feed.profile.post.content.TermMuter
import kotlinx.coroutines.flow.MutableStateFlow

/** Provides and controls configurations of the [SharedPreferences]-specific [TermMuter]. */
internal object SharedPreferencesTermMuter {
  /**
   * Gets the [SharedPreferences] related to the [SharedPreferences]-specific [TermMuter].
   *
   * @param context [Context] through which the [SharedPreferences] will be obtained.
   */
  fun getPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences("shared-preferences-term-muter", Context.MODE_PRIVATE)
  }

  /**
   * Clears all persisted terms.
   *
   * @param context [Context] through which the [SharedPreferences] will be obtained.
   */
  fun reset(context: Context) {
    getPreferences(context).edit(action = SharedPreferences.Editor::clear)
  }
}

/**
 * [TermMuter] that persists muted terms through the [SharedPreferences] API.
 *
 * @param context [Context] through which the [SharedPreferences] will be created.
 */
@Suppress("FunctionName")
fun SharedPreferencesTermMuter(context: Context): TermMuter {
  val preferences = SharedPreferencesTermMuter.getPreferences(context)
  val termsFlow = MutableStateFlow(emptyList<String>())
  return TermMuter {
    getTerms { termsFlow }
    mute {
      preferences.edit { putString(it, it) }
      termsFlow.value += it
    }
    unmute {
      preferences.edit { remove(it) }
      termsFlow.value -= it
    }
  }
}
