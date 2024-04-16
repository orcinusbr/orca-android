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

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import br.com.orcinus.orca.std.injector.Injector
import br.com.orcinus.orca.std.injector.test.InjectorTestRule
import kotlin.test.Test
import org.junit.Rule

internal class ModuleTests {
  @get:Rule val injectorRule = InjectorTestRule()

  @Test
  fun injectsLazily() {
    var hasBeenInjectedLazily = true
    Injector.injectLazily { hasBeenInjectedLazily = false }
    assertThat(hasBeenInjectedLazily).isTrue()
  }

  @Test
  fun getsDependencyInjectedLazily() {
    Injector.injectLazily { 0 }
    assertThat(Injector.get<Int>()).isEqualTo(0)
  }

  @Test
  fun getsDependencyInjectedImmediately() {
    Injector.injectImmediately(0)
    assertThat(Injector.get<Int>()).isEqualTo(0)
  }

  @Test(expected = Module.DependencyNotInjectedException::class)
  fun throwsWhenGettingDependencyThatHasNotBeenInjected() {
    Injector.get<Int>()
  }

  @Test
  fun immediatelyGetsDependencyThatGetsPreviouslyImmediatelyInjectedOne() {
    Injector.injectImmediately(0)
    Injector.injectImmediately(Injector.get<Int>())
    assertThat(Injector.get<Int>()).isEqualTo(0)
  }

  @Test(expected = StackOverflowError::class)
  fun throwsWhenImmediatelyGettingDependencyThatGetsPreviouslyLazilyInjectedOne() {
    Injector.injectImmediately(0)
    Injector.injectLazily { get<Int>() }
    Injector.get<Int>()
  }

  @Test
  fun replacesDependencyInjectedLazily() {
    Injector.injectLazily { 0 }
    Injector.injectLazily { 1 }
    assertThat(Injector.get<Int>()).isEqualTo(1)
  }

  @Test
  fun replacesDependencyInjectedImmediately() {
    Injector.injectImmediately(0)
    Injector.injectImmediately(1)
    assertThat(Injector.get<Int>()).isEqualTo(1)
  }

  @Test(Module.DependencyNotInjectedException::class)
  fun clears() {
    Injector.injectLazily { 0 }
    Injector.clear()
    Injector.get<Int>()
  }
}
