/*
 * Copyright © 2024–2025 Orcinus
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

package br.com.orcinus.orca.feature.postdetails.conversion

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isSameAs
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class PostDetailsConversionScopeTests {
  @Test
  fun providesSameDetailingMultipleTimes() {
    runPostDetailsConversionTest { assertThat(detailed()).isSameAs(detailed()) }
  }

  @Test
  fun favorites() {
    runPostDetailsConversionTest {
      unfavorite()
      post.favorite.get(page = 0).test {
        awaitItem()
        favorite()
        assertThatIDsOf(awaitItem()).containsExactly(actor.id)
      }
    }
  }

  @Test
  fun reposts() {
    runPostDetailsConversionTest {
      unrepost()
      post.repost.get(page = 0).test {
        awaitItem()
        repost()
        assertThatIDsOf(awaitItem()).containsExactly(actor.id)
      }
    }
  }
}
