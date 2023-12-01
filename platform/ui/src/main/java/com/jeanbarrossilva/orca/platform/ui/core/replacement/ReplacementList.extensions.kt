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

package com.jeanbarrossilva.orca.platform.ui.core.replacement

/** Creates an empty [ReplacementList]. */
internal fun <T> emptyReplacementList(): ReplacementList<T, T> {
  return ReplacementList(mutableListOf()) { it }
}

/**
 * Creates an empty [ReplacementList].
 *
 * @param selector Returns the value by which elements should be compared when replacing them.
 */
internal fun <I, O> emptyReplacementList(selector: (I) -> O): ReplacementList<I, O> {
  return ReplacementList(mutableListOf(), selector)
}

/**
 * Creates a [ReplacementList] with the given [elements].
 *
 * @param elements Elements to be added to the [ReplacementList].
 */
internal fun <T> replacementListOf(vararg elements: T): ReplacementList<T, T> {
  return ReplacementList(mutableListOf(*elements)) { it }
}

/**
 * Creates a [ReplacementList] with the given [elements].
 *
 * @param elements Elements to be added to the [ReplacementList].
 * @param selector Returns the value by which elements should be compared when replacing them.
 */
internal fun <I, O> replacementListOf(
  vararg elements: I,
  selector: (I) -> O
): ReplacementList<I, O> {
  return ReplacementList(mutableListOf(*elements), selector)
}
