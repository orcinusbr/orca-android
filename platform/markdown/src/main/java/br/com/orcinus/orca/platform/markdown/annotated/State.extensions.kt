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

package br.com.orcinus.orca.platform.markdown.annotated

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State

/**
 * Builds a [State] by allowing its value to be temporarily changed through [build] and exposing it
 * as being immutable.
 *
 * @param build Lambda from which the resulting [State]'s value can be changed, returning the first
 *   one to which it will be set.
 */
internal fun <T> buildState(build: MutableState<T>.() -> T): State<T> {
  return object : MutableState<T> {
    override var value: T = build()

    override fun component1(): T {
      return value
    }

    override fun component2(): (T) -> Unit {
      return { value = it }
    }
  }
}
