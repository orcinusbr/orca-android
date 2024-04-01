/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
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
