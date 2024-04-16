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

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import br.com.orcinus.orca.std.injector.Injector
import kotlin.test.Test

internal class InjectionExtensionsTests {
  @Test
  fun createsImmediateInjection() {
    assertThat(immediateInjectionOf(0)).isInstanceOf<Injection.Immediate<Int>>()
  }

  @Test
  fun createsLazyInjection() {
    assertThat(lazyInjectionOf { 0 }).isInstanceOf<Injection.Lazy<Int>>()
  }

  @Test
  fun lazyInjectionCreatesDependencyLazily() {
    var hasDependencyBeenCreated = false
    lazyInjectionOf { hasDependencyBeenCreated = true }
    assertThat(hasDependencyBeenCreated).isFalse()
  }

  @Test
  fun lazyInjectionCreatesDependencyOnce() {
    var creationCount = 0
    with(Injector) { with(lazyInjectionOf { creationCount++ }) { repeat(2) { provide() } } }
    assertThat(creationCount).isEqualTo(1)
  }
}
