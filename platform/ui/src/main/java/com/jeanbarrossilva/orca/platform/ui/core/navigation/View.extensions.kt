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

package com.jeanbarrossilva.orca.platform.ui.core.navigation

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import kotlin.reflect.KClass

/**
 * Verifies if this [View]'s (if [isInclusive]) or one of its children's type is equal to [T] and
 * then returns it if it is.
 *
 * This method is run recursively on each child if it's a [ViewGroup] and continues to do so if a
 * child's child is a [ViewGroup], until either the [View] we're searching for is found or none is
 * found at all, in which case an [IllegalStateException] is thrown.
 *
 * @param T [View] to be found.
 * @param isInclusive Whether this [View] should be taken into account in the search and returned if
 *   its type matches [T].
 * @throws IllegalStateException If no matching [View] is found.
 */
internal inline fun <reified T : View> View.get(isInclusive: Boolean = true): T {
  return get(T::class, isInclusive)
}

/** Assigns a generated ID to this [View] if it doesn't already have one. */
internal fun View.identify() {
  if (id == View.NO_ID) {
    id = View.generateViewId()
  }
}

/**
 * Verifies if this [View]'s (if [isInclusive]) or one of its children's [KClass] equal to the given
 * [viewClass].
 *
 * This method is run recursively on each child if it's a [ViewGroup] and continues to do so if a
 * child's child is a [ViewGroup], until either the [View] we're searching for is found or none is
 * found at all, in which case an [IllegalStateException] is thrown.
 *
 * @param T [View] to be found.
 * @param viewClass [KClass] of the [View] to be found.
 * @param isInclusive Whether this [View] should be taken into account in the search and returned if
 *   its [KClass] matches the [viewClass].
 * @throws IllegalStateException If no matching [View] is found.
 */
private fun <T : View> View.get(viewClass: KClass<T>, isInclusive: Boolean): T {
  @Suppress("UNCHECKED_CAST")
  return when {
    isInclusive && this::class == viewClass -> this as T
    this is ViewGroup -> children.firstNotNullOfOrNull { it.get(viewClass, isInclusive = true) }
    else -> null
  }
    ?: throw IllegalStateException("No ${viewClass.simpleName} found from $this.")
}
