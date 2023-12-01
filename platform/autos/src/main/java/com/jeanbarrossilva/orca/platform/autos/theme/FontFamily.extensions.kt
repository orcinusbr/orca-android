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

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.jeanbarrossilva.orca.platform.autos.R

/** [FontFamily] of the Rubik typeface. */
internal val FontFamily.Companion.Rubik
  get() =
    FontFamily(
      Font(R.font.rubik_light, FontWeight.Light),
      Font(R.font.rubik_light_italic, FontWeight.Light, FontStyle.Italic),
      Font(R.font.rubik_normal),
      Font(R.font.rubik_normal_italic, style = FontStyle.Italic),
      Font(R.font.rubik_medium, FontWeight.Medium),
      Font(R.font.rubik_medium_italic, FontWeight.Medium, FontStyle.Italic),
      Font(R.font.rubik_bold, FontWeight.Bold),
      Font(R.font.rubik_bold_italic, FontWeight.Bold, FontStyle.Italic),
      Font(R.font.rubik_extra_bold, FontWeight.ExtraBold),
      Font(R.font.rubik_extra_bold_italic, FontWeight.ExtraBold, FontStyle.Italic),
      Font(R.font.rubik_black, FontWeight.Black),
      Font(R.font.rubik_black_italic, FontWeight.Black, FontStyle.Italic)
    )
