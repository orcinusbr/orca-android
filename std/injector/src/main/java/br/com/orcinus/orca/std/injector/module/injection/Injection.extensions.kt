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

package br.com.orcinus.orca.std.injector.module.injection

import br.com.orcinus.orca.std.injector.module.Module
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Creates an immediate [Injection], which receives a pre-instantiated dependency to be later
 * provided.
 *
 * @param T Dependency to be provided.
 * @param dependency Dependency to be provided and injected.
 * @see Injection.Immediate
 * @see lazyInjectionOf
 */
inline fun <reified T : Any> immediateInjectionOf(dependency: T): Injection<T> {
  return object : Injection.Immediate<T>(dependency) {
    override val dependencyClass = T::class
  }
}

/**
 * Creates an [Injection].
 *
 * @param T Dependency to be injected.
 * @param creation Returns the dependency to be injected that can be lazily retrieved afterwards.
 */
@Deprecated(
  message = "Prefer `lazyInjectionOf` to better distinguish between lazy and immediate injections.",
  ReplaceWith(
    "lazyInjectionOf(creation)",
    "br.com.orcinus.orca.std.injector.module.injection.lazyInjectionOf"
  )
)
@OptIn(ExperimentalContracts::class)
inline fun <reified T : Any> injectionOf(crossinline creation: Module.() -> T): Injection<T> {
  contract { callsInPlace(creation, InvocationKind.AT_MOST_ONCE) }
  return lazyInjectionOf(creation)
}

/**
 * Creates a lazy [Injection], which creates the dependency once and only when it is requested to be
 * provided.
 *
 * @param T Dependency to be injected.
 * @param creation Returns the dependency to be injected that can be lazily retrieved afterwards.
 * @see Injection.Lazy
 * @see immediateInjectionOf
 */
@OptIn(ExperimentalContracts::class)
inline fun <reified T : Any> lazyInjectionOf(crossinline creation: Module.() -> T): Injection<T> {
  contract { callsInPlace(creation, InvocationKind.AT_MOST_ONCE) }
  return object : Injection.Lazy<T>() {
    override val dependencyClass = T::class

    override fun Module.create(): T {
      return creation()
    }
  }
}
