/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.composite.timeline.search.field

import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.loadable.list.SerializableList
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Returns the result of the given [action] that's ran on the populated content; if the
 * [ListLoadable] is not a [ListLoadable.Populated], returns `null`.
 *
 * @param T Element within the [SerializableList].
 * @param R Result produced by the [action].
 * @param action Callback to be run on the populated content.
 * @see ListLoadable.Populated.content
 */
@OptIn(ExperimentalContracts::class)
internal inline fun <T, R> ListLoadable<T>.ifPopulated(action: SerializableList<T>.() -> R): R? {
  contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
  return if (this is ListLoadable.Populated) {
    content.action()
  } else {
    null
  }
}
