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

package com.jeanbarrossilva.orca.platform.navigation

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
