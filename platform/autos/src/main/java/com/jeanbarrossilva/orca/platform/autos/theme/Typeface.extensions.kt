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

package com.jeanbarrossilva.orca.platform.autos.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily

/**
 * Applies the [fontFamily] to each [TextStyle].
 *
 * @param fontFamily [FontFamily] to be applied.
 */
internal fun Typography.with(fontFamily: FontFamily): Typography {
  return copy(
    displayLarge.copy(fontFamily = fontFamily),
    displayMedium.copy(fontFamily = fontFamily),
    displaySmall.copy(fontFamily = fontFamily),
    headlineLarge.copy(fontFamily = fontFamily),
    headlineMedium.copy(fontFamily = fontFamily),
    headlineSmall.copy(fontFamily = fontFamily),
    titleLarge.copy(fontFamily = fontFamily),
    titleMedium.copy(fontFamily = fontFamily),
    titleSmall.copy(fontFamily = fontFamily),
    bodyLarge.copy(fontFamily = fontFamily),
    bodyMedium.copy(fontFamily = fontFamily),
    bodySmall.copy(fontFamily = fontFamily),
    labelLarge.copy(fontFamily = fontFamily),
    labelMedium.copy(fontFamily = fontFamily),
    labelSmall.copy(fontFamily = fontFamily)
  )
}
