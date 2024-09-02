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

package br.com.orcinus.orca.core.feed.profile.post.content

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class TermMuterTests {
  @Test
  fun initialTermsAreEmittedInitially() {
    runTest {
      object : TermMuter() {
          override val initialTerms = hashSetOf("MVI")

          override suspend fun onMuting(term: String) {}

          override suspend fun onUnmuting(term: String) {}
        }
        .termsFlow
        .test { assertThat(awaitItem()).containsExactly("MVI") }
    }
  }

  @Test
  fun emitsListWithMutedTerm() {
    runTest {
      object : TermMuter() {
          override val initialTerms = hashSetOf<String>()

          override suspend fun onMuting(term: String) {}

          override suspend fun onUnmuting(term: String) {}
        }
        .run {
          termsFlow.test {
            awaitItem()
            mute("☠️")
            assertThat(awaitItem()).containsExactly("☠️")
          }
        }
    }
  }

  @Test
  fun emitsListWithoutUnmutedTerm() {
    runTest {
      object : TermMuter() {
          override val initialTerms = hashSetOf<String>()

          override suspend fun onMuting(term: String) {}

          override suspend fun onUnmuting(term: String) {}
        }
        .run {
          termsFlow.test {
            awaitItem()
            mute("💀")
            awaitItem()
            unmute("💀")
            assertThat(awaitItem()).isEmpty()
          }
        }
    }
  }
}
