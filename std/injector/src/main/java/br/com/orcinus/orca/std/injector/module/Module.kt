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

package br.com.orcinus.orca.std.injector.module

import br.com.orcinus.orca.std.injector.module.injection.Injection
import br.com.orcinus.orca.std.injector.module.injection.contains
import br.com.orcinus.orca.std.injector.module.injection.get
import br.com.orcinus.orca.std.injector.module.injection.injectionOf
import kotlin.reflect.KClass

/** Container in which dependencies within a given context can be injected. */
abstract class Module {
  /** [Injection]s that have been created. */
  @PublishedApi internal val injections = mutableListOf<Injection<*>>()

  /**
   * [NoSuchElementException] thrown if a dependency that hasn't been injected is requested to be
   * obtained.
   *
   * @param dependencyClass [KClass] of the requested dependency.
   */
  inner class DependencyNotInjectedException
  @PublishedApi
  internal constructor(dependencyClass: KClass<*>) :
    NoSuchElementException(
      "No dependency of type ${dependencyClass.qualifiedName} has been injected into " +
        "${this::class.simpleName}."
    )

  /**
   * Injects the dependency returned by the [creation].
   *
   * @param T Dependency to be injected.
   * @param creation Returns the dependency to be injected that can be lazily retrieved afterwards.
   */
  inline fun <reified T : Any> inject(crossinline creation: Module.() -> T) {
    val injection = injectionOf(creation)
    inject(injection)
  }

  /**
   * Lazily obtains the injected dependency whose type is [T].
   *
   * @param T Dependency to be lazily obtained.
   */
  inline fun <reified T : Any> lazy(): Lazy<T> {
    return lazy(::get)
  }

  /**
   * Obtains the injected dependency whose type is [T].
   *
   * @param T Dependency to be obtained.
   * @throws DependencyNotInjectedException If no dependency of type [T] has been injected.
   */
  @Throws(NoSuchElementException::class)
  inline fun <reified T : Any> get(): T {
    return injections.get<T>()?.run { provide() } ?: throw DependencyNotInjectedException(T::class)
  }

  /** Removes all [Injection]s. */
  fun clear() {
    injections.clear()
    onClear()
  }

  /**
   * Injects the dependency returned by the [injection].
   *
   * @param T Dependency to be injected.
   * @param dependencyClass [KClass] to which the dependency will be associated.
   * @param injection Returns the dependency to be injected.
   */
  @PublishedApi
  internal inline fun <reified T : Any> inject(injection: Injection<T>) {
    val hasNotBeenInjected = !injections.contains<T>()
    if (hasNotBeenInjected) {
      injections.add(injection)
    }
  }

  /** Callback run whenever this [Module] is cleared. */
  internal open fun onClear() {}
}
