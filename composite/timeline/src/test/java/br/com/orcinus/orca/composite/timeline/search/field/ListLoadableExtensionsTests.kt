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

package br.com.orcinus.orca.composite.timeline.search.field

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isFalse
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.loadable.list.SerializableList
import com.jeanbarrossilva.loadable.list.serializableListOf
import kotlin.test.Test

internal class ListLoadableExtensionsTests {
  @Test
  fun doesNotRunActionWhenLoading() {
    var hasRunAction = false
    ListLoadable.Loading<Int>().ifPopulated { hasRunAction = true }
    assertThat(hasRunAction).isFalse()
  }

  @Test
  fun doesNotRunActionWhenEmpty() {
    var hasRunAction = false
    ListLoadable.Empty<Int>().ifPopulated { hasRunAction = true }
    assertThat(hasRunAction).isFalse()
  }

  @Test
  fun doesNotRunActionWhenFailed() {
    var hasRunAction = false
    ListLoadable.Failed<Int>(Exception()).ifPopulated { hasRunAction = true }
    assertThat(hasRunAction).isFalse()
  }

  @Test
  fun runsActionWhenPopulated() {
    lateinit var content: SerializableList<Int>
    ListLoadable.Populated(serializableListOf(0)).ifPopulated { content = this }
    assertThat(content).containsExactly(0)
  }
}
