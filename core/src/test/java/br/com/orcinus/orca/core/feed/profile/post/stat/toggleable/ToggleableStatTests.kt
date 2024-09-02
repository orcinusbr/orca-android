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

package br.com.orcinus.orca.core.feed.profile.post.stat.toggleable

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import kotlin.test.Test
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest

internal class ToggleableStatTests {
  @Test
  fun isInitiallyDisabled() {
    runTest {
      object : ToggleableStat<Any>(count = 0) {
          override fun get(page: Int): Flow<List<Any>> {
            return emptyFlow()
          }

          override suspend fun onSetEnabled(isEnabled: Boolean) {}
        }
        .isEnabledFlow
        .test { assertThat(awaitItem()).isFalse() }
    }
  }

  @Test
  fun enables() {
    object : ToggleableStat<Any>(count = 0) {
        override fun get(page: Int): Flow<List<Any>> {
          return emptyFlow()
        }

        override suspend fun onSetEnabled(isEnabled: Boolean) {}
      }
      .apply {
        runTest {
          isEnabledFlow.test {
            awaitItem()
            enable()
            assertThat(awaitItem()).isTrue()
          }
        }
      }
  }

  @Test
  fun disables() {
    object : ToggleableStat<Any>(count = 0) {
        override fun get(page: Int): Flow<List<Any>> {
          return emptyFlow()
        }

        override suspend fun onSetEnabled(isEnabled: Boolean) {}
      }
      .apply {
        runTest {
          enable()
          isEnabledFlow.test {
            awaitItem()
            disable()
            assertThat(awaitItem()).isFalse()
          }
        }
      }
  }
}
