/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.text.size

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme

/**
 * Remembers an [AutoSizeRange], defaulting the [minimum][AutoSizeRange.min] size to
 * [AutosTheme.typography]'s [Typography.labelSmall]'s.
 *
 * @param max Maximum, default size.
 */
@Composable
fun rememberAutoSizeRange(max: TextUnit): AutoSizeRange {
  return rememberAutoSizeRange(min = AutosTheme.typography.labelSmall.fontSize, max)
}

/**
 * Remembers an [AutoSizeRange].
 *
 * @param min Minimum size.
 * @param max Maximum, default size.
 */
@Composable
fun rememberAutoSizeRange(min: TextUnit, max: TextUnit): AutoSizeRange {
  val density = LocalDensity.current
  return remember(density, min, max) { AutoSizeRange(density, min, max) }
}
