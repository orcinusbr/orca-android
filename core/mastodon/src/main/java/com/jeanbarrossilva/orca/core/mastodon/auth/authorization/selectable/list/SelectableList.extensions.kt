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
