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

package br.com.orcinus.orca.std.injector.module.binding

import br.com.orcinus.orca.std.injector.module.Module
import kotlin.reflect.KClass

/**
 * Creates a [Binding] whose base and alias [KClass]es are both this [Module]'s.
 *
 * @param T [Module] to be bound to its own type.
 * @see Binding.base
 * @see Binding.alias
 */
val <T : Module> T.bound
  @Suppress("UNCHECKED_CAST")
  get() = Binding(base = this::class as KClass<T>, alias = this::class as KClass<T>, target = this)

/**
 * Creates a [Binding] between this [Module]'s actual type (to be used as the alias) and the
 * specified base one.
 *
 * @param B Base [Module] to which this [Module] will be bound to.
 * @param A [Module] at the utmost bottom of the inheritance tree to which this [Module] will be
 *   bound to.
 * @see Binding.alias
 * @see Binding.base
 */
inline fun <reified B : Module, A : B> A.boundTo(): Binding<B, A> {
  @Suppress("UNCHECKED_CAST")
  return Binding(base = B::class, alias = this::class as KClass<A>, target = this)
}
