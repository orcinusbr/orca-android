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
