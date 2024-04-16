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

/**
 * Creates an immediate [Injection], which receives a pre-instantiated dependency to be later
 * provided.
 *
 * @param T Dependency to be provided.
 * @param dependency Dependency to be provided and injected.
 * @see Injection.Immediate
 * @see lazyInjectionOf
 */
fun <T : Any> immediateInjectionOf(dependency: T): Injection<T> {
  return Injection.Immediate(dependency)
}

/**
 * Creates an [Injection].
 *
 * @param T Dependency to be injected.
 * @param creation Returns the dependency to be injected that can be lazily retrieved afterwards.
 */
@Deprecated(
  message = "Use `lazyInjectionOf` to better distinguish between lazy and immediate injections.",
  ReplaceWith(
    "lazyInjectionOf(creation)",
    "br.com.orcinus.orca.std.injector.module.injection.lazyInjectionOf"
  )
)
inline fun <reified T : Any> injectionOf(crossinline creation: Module.() -> T): Injection<T> {
  return lazyInjectionOf(creation)
}

/**
 * Creates a lazy [Injection], which only creates the dependency when it is requested to be
 * provided.
 *
 * @param T Dependency to be injected.
 * @param creation Returns the dependency to be injected that can be lazily retrieved afterwards.
 * @see Injection.Lazy
 * @see immediateInjectionOf
 */
inline fun <reified T : Any> lazyInjectionOf(crossinline creation: Module.() -> T): Injection<T> {
  return object : Injection.Lazy<T>() {
    override val dependencyClass = T::class

    override fun Module.create(): T {
      return creation()
    }
  }
}
