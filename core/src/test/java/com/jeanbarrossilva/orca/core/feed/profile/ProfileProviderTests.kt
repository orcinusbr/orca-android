package com.jeanbarrossilva.orca.core.feed.profile

import com.jeanbarrossilva.orca.core.sample.feed.profile.sample
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
      }
    runTest { assertEquals(Profile.sample, provider.provide(Profile.sample.id).first()) }
  }
}
