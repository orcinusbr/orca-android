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

package br.com.orcinus.orca.std.injector.module.replacement

import assertk.assertThat
import assertk.assertions.isInstanceOf
import kotlin.test.Test

internal class ReplacerTests {
  @Test
  fun enablesCachingWhenCreatingReplacerWithCustomSelector() {
    val selector = Any::hashCode
    Replacer.withCustomSelector(selector) {
      assertThat(it).isInstanceOf<Replacer.Caching.Enabled<Any, Int>>()
      mutableReplacementListOf(selector = selector)
    }
  }

  @Test
  fun disablesCachingWhenCreatingReplacerWithStructuralEqualityBasedSelector() {
    Replacer.withStructuralEqualityBasedSelector { caching, selector ->
      assertThat(caching).isInstanceOf<Replacer.Caching.Disabled<Any, Any>>()
      EmptyReplacer<Any, Any>(caching, selector)
    }
  }
}
