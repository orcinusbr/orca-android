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

package br.com.orcinus.orca.core.feed.profile

import br.com.orcinus.orca.core.sample.test.feed.profile.sample
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

internal class ProfileProviderTests {
  @Test
  fun `GIVEN a nonexistent profile WHEN requesting it to be provided THEN it throws`() {
    val provider =
      object : ProfileProvider() {
        override suspend fun contains(id: String): Boolean {
          return false
        }

        override suspend fun onProvide(id: String): Flow<Profile> {
          return emptyFlow()
        }

        override fun createNonexistentProfileException(): NonexistentProfileException {
          return NonexistentProfileException(cause = null)
        }
      }
    assertFailsWith<ProfileProvider.NonexistentProfileException> {
      runTest { provider.provide("🫥") }
    }
  }

  @Test
  fun `GIVEN a profile WHEN requesting it to be provided THEN it's provided`() {
    val provider =
      object : ProfileProvider() {
        override suspend fun contains(id: String): Boolean {
          return true
        }

        override suspend fun onProvide(id: String): Flow<Profile> {
          return flowOf(Profile.sample)
        }

        override fun createNonexistentProfileException(): NonexistentProfileException {
          return NonexistentProfileException(cause = null)
        }
      }
    runTest { assertEquals(Profile.sample, provider.provide(Profile.sample.id).first()) }
  }
}
