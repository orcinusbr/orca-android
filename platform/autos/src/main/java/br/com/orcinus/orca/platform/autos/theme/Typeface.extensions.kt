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
