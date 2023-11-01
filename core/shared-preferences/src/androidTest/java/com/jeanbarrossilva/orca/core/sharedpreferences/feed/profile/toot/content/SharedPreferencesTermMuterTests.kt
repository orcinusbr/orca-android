package com.jeanbarrossilva.orca.core.sharedpreferences.feed.profile.toot.content

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
