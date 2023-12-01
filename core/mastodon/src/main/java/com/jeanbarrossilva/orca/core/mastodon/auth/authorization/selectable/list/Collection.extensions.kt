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

/**
 * Converts this [Collection] into a [SelectableList] and selects the given [element].
 *
 * @param element Element to be selected.
 */
internal fun <T> Collection<T>.select(element: T): SelectableList<T> {
  return selectIf { _, current -> element == current }
}

/** Converts this [Collection] into a [SelectableList] and selects its first element. */
internal fun <T> Collection<T>.selectFirst(): SelectableList<T> {
  return selectIf { index, _ -> index == 0 }
}

/**
 * Converts this [Collection] into a [SelectableList].
 *
 * @param selection Predicate that determines whether the element that's been given to it is
 *   selected.
 */
private fun <T> Collection<T>.selectIf(selection: (index: Int, T) -> Boolean): SelectableList<T> {
  val elements = mapIndexed { index, element ->
    @Suppress("DiscouragedApi") Selectable(element, selection(index, element))
  }

  @Suppress("DiscouragedApi") return SelectableList(elements)
}
