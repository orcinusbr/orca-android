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
import br.com.orcinus.orca.std.injector.module.injection.SomeInjection
import br.com.orcinus.orca.std.injector.module.injection.immediateInjectionOf
import br.com.orcinus.orca.std.injector.module.injection.lazyInjectionOf
import br.com.orcinus.orca.std.injector.module.replacement.replacementListOf
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KClass

/** Container in which dependencies within a given context can be injected. */
abstract class Module {
  /** [Injection]s that have been created. */
  @PublishedApi
  internal val injections = replacementListOf(selector = SomeInjection::dependencyClass)

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
  @Deprecated(
    message = "Prefer `injectLazily` to better distinguish between immediate and lazy injections.",
    ReplaceWith("injectLazily(creation)")
  )
  @OptIn(ExperimentalContracts::class)
  inline fun <reified T : Any> inject(crossinline creation: Module.() -> T) {
    contract { callsInPlace(creation, InvocationKind.AT_MOST_ONCE) }
    injectLazily(creation)
  }

  /**
   * Injects the dependency returned by the [creation] lazily, creating it only when it is requested
   * to be obtained.
   *
   * @param T Dependency to be injected.
   * @param creation Returns the dependency to be injected that can be lazily retrieved afterwards.
   * @see get
   * @see injectImmediately
   */
  @OptIn(ExperimentalContracts::class)
  inline fun <reified T : Any> injectLazily(crossinline creation: Module.() -> T) {
    contract { callsInPlace(creation, InvocationKind.AT_MOST_ONCE) }
    val injection = lazyInjectionOf(creation)
    inject(injection)
  }

  /**
   * Injects the [dependency] immediately.
   *
   * @param T Dependency to be injected.
   * @param dependency Dependency to be immediately injected.
   * @see injectLazily
   */
  inline fun <reified T : Any> injectImmediately(dependency: T) {
    val injection = immediateInjectionOf(dependency)
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
  @Throws(DependencyNotInjectedException::class)
  inline fun <reified T : Any> get(): T {
    return getOrNull() ?: throw DependencyNotInjectedException(T::class)
  }

  /** Removes all [Injection]s. */
  fun clear() {
    injections.clear()
    onClear()
  }

  /**
   * Injects the dependency returned by the [injection].
   *
   * @param injection Provides the dependency to be injected.
   */
  @PublishedApi
  internal fun inject(injection: SomeInjection) {
    injections.add(injection)
  }

  /**
   * Obtains a dependency of type [T] that has been previously injected into this [Module] or `null`
   * if none has been.
   *
   * @param T Dependency to be obtained.
   */
  @PublishedApi
  internal inline fun <reified T : Any> getOrNull(): T? {
    return injections
      .filterIsInstance<Injection<T>>()
      .singleOrNull { it.dependencyClass == T::class }
      ?.run { provide() }
  }

  /** Callback run whenever this [Module] is cleared. */
  internal open fun onClear() {}
}
