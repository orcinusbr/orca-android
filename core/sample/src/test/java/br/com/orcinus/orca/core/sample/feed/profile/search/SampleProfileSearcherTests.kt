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

package br.com.orcinus.orca.core.sample.feed.profile.search

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.containsExactly
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearchResult
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.sample.feed.profile.account.sample
import br.com.orcinus.orca.core.sample.test.feed.profile.search.sample
import br.com.orcinus.orca.core.sample.test.instance.sample
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class SampleProfileSearcherTests {
  @Test
  fun searches() {
    runTest {
      Instance.sample.profileSearcher.search("${Account.sample}").test {
        assertThat(awaitItem()).containsExactly(ProfileSearchResult.sample)
      }
    }
  }
}
