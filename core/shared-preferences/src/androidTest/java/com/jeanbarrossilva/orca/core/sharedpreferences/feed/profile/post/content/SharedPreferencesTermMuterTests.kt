/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.core.sharedpreferences.feed.profile.post.content

import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import com.jeanbarrossilva.orca.core.sharedpreferences.actor.test.SharedPreferencesCoreTestRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

internal class SharedPreferencesTermMuterTests {
  private val context
    get() = InstrumentationRegistry.getInstrumentation().context

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
