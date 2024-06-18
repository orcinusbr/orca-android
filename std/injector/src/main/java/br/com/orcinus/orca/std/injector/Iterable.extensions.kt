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

package br.com.orcinus.orca.std.injector

import br.com.orcinus.orca.std.injector.module.Inject
import br.com.orcinus.orca.std.injector.module.Module
import br.com.orcinus.orca.std.injector.module.injection.Injection
import br.com.orcinus.orca.std.injector.module.injection.lazyInjectionOf
import kotlin.reflect.KProperty1
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf

/**
 * Returns a [List] containing only [KProperty1]s that characterize an injection into a [Module].
 * Those...
 * - are annotated with [Inject], denoting that the dependencies provided by their values should be
 *   automatically injected into the [Module] in which they were declared; and
 * - have a return type of [Injection], which allows for operations to be performed within the
 *   [Module] (when it is a lazy one) and provides a dependency.
 *
 * @param M [Module] into which the resulting dependencies of the [KProperty1]s' values will be
 *   injected.
 * @see lazyInjectionOf
 */
@PublishedApi
internal fun <M : Module> Iterable<KProperty1<out M, *>>.filterIsInjection():
  List<KProperty1<M, Injection<Any>>> {
  @Suppress("UNCHECKED_CAST")
  return filter {
    it.any(delimiter = typeOf<Module>()) { hasAnnotation<Inject>() } &&
      it.returnType.jvmErasure == Injection::class
  }
    as List<KProperty1<M, Injection<Any>>>
}
