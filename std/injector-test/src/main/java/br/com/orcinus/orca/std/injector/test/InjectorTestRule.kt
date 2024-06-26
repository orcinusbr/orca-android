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

package br.com.orcinus.orca.std.injector.test

import br.com.orcinus.orca.std.injector.Injector
import org.junit.rules.ExternalResource

/**
 * Rule for running the [action] before and clearing the [Injector] after the test is done.
 *
 * @param action Operation to be performed on the [Injector].
 */
class InjectorTestRule(private val action: Injector.() -> Unit = {}) : ExternalResource() {
  override fun before() {
    Injector.action()
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
