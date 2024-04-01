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

package br.com.orcinus.orca.platform.starter.lifecycle.state

/**
 * Compares this [CompleteLifecycleState] with the given [other]; if it is `null`, then it is
 * considered to be less than [other].
 *
 * @param other [CompleteLifecycleState] to compare this one with.
 */
infix operator fun CompleteLifecycleState?.compareTo(other: CompleteLifecycleState): Int {
  return this?.compareTo(other) ?: -1
}

/**
 * Provides the [CompleteLifecycleState] that succeeds this one; if it's `null`, provides the
 * [created][CompleteLifecycleState.CREATED] [CompleteLifecycleState].
 */
internal fun CompleteLifecycleState?.next(): CompleteLifecycleState? {
  return if (this == null) CompleteLifecycleState.CREATED else next()
}
