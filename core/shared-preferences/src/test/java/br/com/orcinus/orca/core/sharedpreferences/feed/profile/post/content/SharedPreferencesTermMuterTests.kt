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

package br.com.orcinus.orca.core.sharedpreferences.feed.profile.post.content

import br.com.orcinus.orca.core.sharedpreferences.actor.SharedPreferencesCoreTestRule
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class SharedPreferencesTermMuterTests {
  @get:Rule val coreRule = SharedPreferencesCoreTestRule()

  @Test
  fun persistsMutedTerm() {
    runTest {
      coreRule.termMuter.mute("🐝")
      assertEquals("🐝", coreRule.termMuter.preferences.getString("🐝", null))
    }
  }

  @Test
  fun removesUnmutedTerm() {
    runTest {
      coreRule.termMuter.mute("👒")
      coreRule.termMuter.unmute("👒")
      assertNull(coreRule.termMuter.preferences.getString("👒", null))
    }
  }
}
