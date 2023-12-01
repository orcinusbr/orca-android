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

package com.jeanbarrossilva.orca.std.injector

/**
 * Casts this to [T].
 *
 * @param T Value to which this will be casted.
 * @throws ClassCastException If this cannot be casted to [T].
 */
@PublishedApi
@Throws(ClassCastException::class)
internal fun <T> Any.castTo(): T {
  @Suppress("UNCHECKED_CAST") return this as T
}
