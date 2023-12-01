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

package com.jeanbarrossilva.orca.std.injector.test

import com.jeanbarrossilva.orca.std.injector.Injector
import org.junit.rules.ExternalResource

/**
 * Rule for running the [injection] before and clearing the [Injector] after the test is done.
 *
 * @param injection Operation to be performed on the [Injector].
 */
class InjectorTestRule(private val injection: Injector.() -> Unit = {}) : ExternalResource() {
  override fun before() {
    Injector.injection()
  }

  override fun after() {
    Injector.clear()
  }

  /**
   * Surrounds the execution of the [use] lambda with a call to [before] that precedes it and
   * another to [after] that succeeds it.
   *
   * @param use Operation to be performed.
   */
  internal fun use(use: () -> Unit = {}) {
    before()
    use()
    after()
  }
}
