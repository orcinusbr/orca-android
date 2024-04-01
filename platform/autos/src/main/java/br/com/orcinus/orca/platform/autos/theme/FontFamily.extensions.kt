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

package br.com.orcinus.orca.platform.autos.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import br.com.orcinus.orca.platform.autos.R

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
