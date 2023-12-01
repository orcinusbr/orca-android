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

@file:JvmName("BordersExtensions")

package com.jeanbarrossilva.orca.platform.autos.borders

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.jeanbarrossilva.orca.autos.borders.Borders
import com.jeanbarrossilva.orca.platform.autos.noLocalProvidedFor

/** [CompositionLocal] that provides [Borders]. */
internal val LocalBorders = compositionLocalOf<Borders> { noLocalProvidedFor("LocalBorders") }

/** Whether components should have the [BorderStroke]s applied to them. */
internal val Borders.Companion.areApplicable
  @Composable get() = areApplicable(LocalContext.current)

/**
 * Checks whether components should have the [BorderStroke]s applied to them.
 *
 * @param context [Context] through which the theme in which the system currently is will be
 *   obtained.
 */
@JvmName("areApplicable")
internal fun Borders.Companion.areApplicable(context: Context): Boolean {
  return with(context.resources.configuration) {
    uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_NO
  }
}
