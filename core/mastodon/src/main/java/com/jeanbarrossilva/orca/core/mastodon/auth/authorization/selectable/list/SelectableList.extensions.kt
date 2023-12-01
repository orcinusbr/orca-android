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

package com.jeanbarrossilva.orca.core.mastodon.auth.authorization.selectable.list

import com.jeanbarrossilva.orca.core.mastodon.auth.authorization.selectable.Selectable

/** Creates an empty [SelectableList]. */
internal inline fun <reified T> emptySelectableList(): SelectableList<T> {
  return selectableListOf()
}

/**
 * Creates a [SelectableList] with the given [elements].
 *
 * @param elements [Selectable]s to be put in the [SelectableList].
 */
internal fun <T> selectableListOf(vararg elements: Selectable<T>): SelectableList<T> {
  val list = listOf(*elements)

  @Suppress("DiscouragedApi") return SelectableList(list)
}
