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

package br.com.orcinus.orca.platform.autos.kit.input.text.composition.interop

import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.`if`
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Returns the result of the given [transform] if the result of [condition] is `true`; otherwise,
 * returns the receiver.
 *
 * @param T Object to be transformed in case the [condition] returns `true`.
 * @param condition Determines whether or not the result of [transform] will get returned.
 * @param transform Transformation to be made to the receiver.
 */
@OptIn(ExperimentalContracts::class)
@PublishedApi
internal inline fun <T> T.`if`(condition: T.() -> Boolean, transform: T.() -> T): T {
  contract {
    callsInPlace(condition, InvocationKind.EXACTLY_ONCE)
    callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
  }
  return `if`(condition(), transform)
}
