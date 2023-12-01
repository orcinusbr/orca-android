/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
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
