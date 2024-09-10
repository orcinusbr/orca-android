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

package br.com.orcinus.orca.core.feed.profile.post.stat.addable

import assertk.assertThat
import assertk.assertions.isTrue
import kotlin.test.Test
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest

internal class AddableStatTests {
  @Test
  fun adds() {
    var hasAdded = false
    val stat =
      object : AddableStat<Byte>(count = 0) {
        override fun get(page: Int): Flow<List<Byte>> {
          return emptyFlow()
        }

        override suspend fun onAddition(element: Byte) {
          hasAdded = true
        }

        override suspend fun onRemoval(element: Byte) {}
      }
    runTest { stat.add(0) }
    assertThat(hasAdded).isTrue()
  }

  @Test
  fun removes() {
    var hasRemoved = false
    val stat =
      object : AddableStat<Byte>(count = 0) {
        override fun get(page: Int): Flow<List<Byte>> {
          return emptyFlow()
        }

        override suspend fun onAddition(element: Byte) {}

        override suspend fun onRemoval(element: Byte) {
          hasRemoved = true
        }
      }
    runTest { stat.remove(0) }
    assertThat(hasRemoved).isTrue()
  }
}
