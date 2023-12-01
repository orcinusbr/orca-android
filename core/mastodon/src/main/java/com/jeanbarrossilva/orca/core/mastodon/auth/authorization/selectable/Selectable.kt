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

package com.jeanbarrossilva.orca.core.mastodon.auth.authorization.selectable

import androidx.annotation.Discouraged

/**
 * Holds a [value] that can be selected.
 *
 * @param value Value that's selected or unselected, according to [isSelected].
 * @param isSelected Whether [value] is considered to be selected.
 */
internal data class Selectable<T>
@Discouraged("Use the `select` or `unselect` extension functions instead.")
constructor(val value: T, val isSelected: Boolean)
