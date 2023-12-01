/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.std.injector

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jeanbarrossilva.orca.std.injector.module.Module
import com.jeanbarrossilva.orca.std.injector.test.InjectorTestRule
import kotlin.test.Test
import org.junit.Rule

internal class ModuleTests {
  @get:Rule val injectorRule = InjectorTestRule()

  @Test
  fun injectsDependency() {
    Injector.inject { 0 }
    assertThat(Injector.get<Int>()).isEqualTo(0)
  }

  @Test(expected = Module.DependencyNotInjectedException::class)
  fun throwsWhenGettingDependencyThatHasNotBeenInjected() {
    Injector.get<Int>()
  }

  @Test
  fun getsDependencyThatGetsPreviouslyInjectedOne() {
    Injector.inject { 0 }
    Injector.inject { get<Int>() }
    assertThat(Injector.get<Int>()).isEqualTo(0)
  }

  @Test(Module.DependencyNotInjectedException::class)
  fun clears() {
    Injector.inject { 0 }
    Injector.clear()
    Injector.get<Int>()
  }
}
