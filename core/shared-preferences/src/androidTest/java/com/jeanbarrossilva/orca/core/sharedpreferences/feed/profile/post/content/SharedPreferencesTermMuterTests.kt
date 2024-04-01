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

package com.jeanbarrossilva.orca.core.sharedpreferences.feed.profile.post.content

import app.cash.turbine.test
import com.jeanbarrossilva.orca.core.sharedpreferences.actor.SharedPreferencesCoreTestRule
import com.jeanbarrossilva.orca.platform.testing.context
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

internal class SharedPreferencesTermMuterTests {
  @get:Rule val coreRule = SharedPreferencesCoreTestRule()

  @Test
  fun persistsMutedTerm() {
    runTest {
      coreRule.termMuter.mute("🐝")
      assertEquals("🐝", SharedPreferencesTermMuter.getPreferences(context).getString("🐝", null))
    }
  }

  @Test
  fun emitsListWithMutedTerm() {
    runTest {
      coreRule.termMuter.mute("☠️")
      coreRule.termMuter.getTerms().test { assertEquals(listOf("☠️"), awaitItem()) }
    }
  }

  @Test
  fun removesUnmutedTerm() {
    runTest {
      coreRule.termMuter.mute("👒")
      coreRule.termMuter.unmute("👒")
      assertNull(SharedPreferencesTermMuter.getPreferences(context).getString("👒", null))
    }
  }

  @Test
  fun emitsListWithoutUnmutedTerm() {
    runTest {
      coreRule.termMuter.mute("💀")
      coreRule.termMuter.unmute("💀")
      coreRule.termMuter.getTerms().test { assertEquals(emptyList<String>(), awaitItem()) }
    }
  }
}
