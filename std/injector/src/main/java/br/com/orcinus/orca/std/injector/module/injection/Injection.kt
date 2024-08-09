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
import kotlin.reflect.KClass

/** [Injection] with a generic dependency. */
internal typealias SomeInjection = Injection<*>

/**
 * Provides the dependency to be injected into a given [Module].
 *
 * @param T Dependency to be created and provided.
 * @see provide
 * @see immediateInjectionOf
 * @see lazyInjectionOf
 */
sealed class Injection<T : Any> {
  /** [KClass] of the dependency. */
  @PublishedApi internal abstract val dependencyClass: KClass<T>

  /**
   * [Injection] that provides the already evaluated [dependency] each time it is requested to be
   * either created or retrieved.
   *
   * @param T Dependency to be provided.
   * @param dependency Pre-instantiated dependency to be provided and injected.
   */
  @PublishedApi
  internal abstract class Immediate<T : Any>(private val dependency: T) : Injection<T>() {
    override fun Module.provide(): T {
      return create()
    }

    override fun Module.create(): T {
      return dependency
    }
  }

  /**
   * [Injection] that creates the dependency once and only when it is requested to be provided.
   *
   * @param T Dependency to be created and provided.
   * @see create
   * @see provide
   */
  @PublishedApi
  internal abstract class Lazy<T : Any> : Injection<T>() {
    /** Dependency to be later retrieved. */
    private var dependency: T? = null

    override fun Module.provide(): T {
      return dependency ?: create().also { dependency = it }
    }
  }

  /**
   * Creates the dependency to be injected or retrieves if this is a lazy [Injection] it is the
   * first time that it's being requested to be provided.
   *
   * @see create
   */
  @PublishedApi internal abstract fun Module.provide(): T

  /** Creates the dependency to be injected. */
  protected abstract fun Module.create(): T
}
