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

package com.jeanbarrossilva.orca.std.injector.module.binding

import com.jeanbarrossilva.orca.std.injector.module.Module

/**
 * Creates a [Binding] between this [Module]'s actual type and the specified base one.
 *
 * @param B Base [Module] to which this [Module] will be bound to.
 * @param A [Module] at the utmost bottom of the inheritance tree to which the [target] will be
 *   bound to.
 */
inline fun <reified B : Module, reified A : B> A.boundTo(): Binding<B, A> {
  return Binding(B::class, A::class, this)
}
