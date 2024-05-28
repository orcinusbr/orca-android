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

package br.com.orcinus.orca.core.mastodon.network.request.headers.pool

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.ktor.utils.io.pool.useInstance
import io.mockk.spyk
import io.mockk.verify
import kotlin.test.Test

internal class ObjectPoolExtensionsTests {
  @Test
  fun creates() {
    objectPoolOf { 0 }
      .use { pool -> pool.useInstance { number -> assertThat(number).isEqualTo(0) } }
  }

  @Test
  fun maps() {
    objectPoolOf { 2 }
      .map { it * it }
      .use { pool -> pool.useInstance { number -> assertThat(number).isEqualTo(4) } }
  }

  @Test
  fun mappedPoolHasSameCapacityAsOriginalOne() {
    objectPoolOf(256) { 0 }.map { it * it }.use { assertThat(it.capacity).isEqualTo(256) }
  }

  @Test
  fun mappedPoolBorrowsFromOriginalOneWhenBorrowing() {
    spyk(objectPoolOf { 2 }) {
      map { it * it }.use { it.useInstance { _ -> verify(exactly = 1) { it.borrow() } } }
    }
  }

  @Test
  fun mappedPoolDisposesOriginalOneWhenDisposed() {
    spyk(objectPoolOf { 2 }) {
      map { it / it }.dispose()
      verify { dispose() }
    }
  }
}
