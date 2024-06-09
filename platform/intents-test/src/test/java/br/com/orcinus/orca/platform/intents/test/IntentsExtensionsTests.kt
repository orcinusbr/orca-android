/*
 * Copyright © 2023-2024 Orcinus
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

package br.com.orcinus.orca.platform.intents.test

import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import br.com.orcinus.orca.ext.uri.URIBuilder
import br.com.orcinus.orca.platform.starter.StartableActivity
import io.mockk.mockkStatic
import io.mockk.verify
import junit.framework.AssertionFailedError
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class IntentsExtensionsTests {
  @Test
  fun initializesEspressoIntentsBeforeIntending() {
    mockkStatic(Intents::class) {
      intend(anyIntent()) {
        startActivity()
        verify { Intents.init() }
      }
    }
  }

  @Test
  fun releasesEspressoIntentsAfterIntending() {
    mockkStatic(Intents::class) {
      intend(anyIntent()) { startActivity() }
      verify { Intents.release() }
    }
  }

  @Test
  fun releasesEspressoIntentsAfterActionThrows() {
    mockkStatic(Intents::class) {
      try {
        intend(anyIntent()) {}
      } catch (_: AssertionFailedError) {
        verify { Intents.release() }
      }
    }
  }

  @Test(expected = AssertionFailedError::class)
  fun throwsWhenBrowsingIsIntendedButNotRequested() {
    intendBrowsingTo(URIBuilder.url().scheme("https").host("orca.jeanbarrossilva.com").build()) {}
  }

  @Test
  fun intendsBrowsing() {
    val uri = URIBuilder.url().scheme("https").host("orca.jeanbarrossilva.com").build()
    intendBrowsingTo(uri) { browseTo(uri) }
  }

  @Test(expected = AssertionFailedError::class)
  fun throwsWhenActivityIsIntendedToBeStartedButIsNot() {
    intendStartingOf<StartableActivity> {}
  }

  @Test
  fun intendsStartingOfActivity() {
    intendStartingOf<StartableActivity> { startActivity() }
  }
}
