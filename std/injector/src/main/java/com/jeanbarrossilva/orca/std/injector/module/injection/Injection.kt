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

package com.jeanbarrossilva.orca.std.injector.module.injection

import com.jeanbarrossilva.orca.std.injector.module.Module
import kotlin.reflect.KClass

/**
 * Lazily provides the dependency to be injected into a given [Module].
 *
 * @param T Dependency to be injected.
 * @see injectionOf
 * @see provide
 */
abstract class Injection<T : Any> @PublishedApi internal constructor() {
  /** Dependency to be injected. */
  private var dependency: T? = null

  /** [KClass] of the [dependency]. */
  @PublishedApi internal abstract val dependencyClass: KClass<T>

  /**
   * Retrieves the dependency to be injected or creates it if it's the first time that it's being
   * requested to be provided.
   *
   * @see create
   */
  fun Module.provide(): T {
    return dependency ?: create().also { dependency = it }
  }

  /** Creates the dependency to be injected. */
  protected abstract fun Module.create(): T
}
