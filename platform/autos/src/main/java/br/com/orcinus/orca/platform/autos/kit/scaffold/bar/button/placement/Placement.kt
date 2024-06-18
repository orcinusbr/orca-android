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

package br.com.orcinus.orca.platform.autos.kit.scaffold.bar.button.placement

import androidx.compose.ui.layout.Placeable

/**
 * Indicates where the [placeable] will be placed in relation to previously placed ones.
 *
 * @param placeable [Placeable] yet to be placed.
 * @param axisOffset Additional horizontal or vertical shift in pixels.
 */
internal data class Placement(val placeable: Placeable, val axisOffset: Int)
