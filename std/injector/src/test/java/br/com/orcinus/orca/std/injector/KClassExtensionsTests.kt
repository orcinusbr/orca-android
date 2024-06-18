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

package br.com.orcinus.orca.std.injector

import assertk.assertThat
import assertk.assertions.containsExactly
import kotlin.reflect.typeOf
import kotlin.test.Test

internal class KClassExtensionsTests {
  private abstract class Animal {
    abstract val isAwesome: Boolean
  }

  private abstract class Dolphin : Animal()

  private class Orca : Dolphin() {
    override val isAwesome = true
  }

  @Test
  fun getsPropertiesOverriddenByAnother() {
    assertThat(Orca::class.getPropertiesOverriddenBy(Orca::isAwesome, delimiter = typeOf<Animal>()))
      .containsExactly(Animal::isAwesome, Dolphin::isAwesome)
  }
}
