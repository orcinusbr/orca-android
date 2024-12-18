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

package br.com.orcinus.orca.composite.timeline.test.search.field

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import br.com.orcinus.orca.composite.timeline.search.field.ResultSearchTextField
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearchResult
import br.com.orcinus.orca.core.sample.feed.profile.account.sample
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.core.sample
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.loadable.list.serializableListOf
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class SemanticsMatcherExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun matchesDismissButton() {
    composeRule
      .apply { setContent { AutosTheme { ResultSearchTextField() } } }
      .onNode(isDismissButton())
      .assertIsDisplayed()
  }

  @Test
  fun doesNotMatchNonResultSearchTextField() =
    composeRule
      .apply { setContent { CircularProgressIndicator() } }
      .onNode(isResultSearchTextField())
      .assertDoesNotExist()

  @Test
  fun matchesResultSearchTextField() {
    composeRule
      .apply { setContent { AutosTheme { ResultSearchTextField() } } }
      .onNode(isResultSearchTextField())
      .assertIsDisplayed()
  }

  @Test
  fun matchesResultCard() {
    composeRule
      .apply {
        setContent {
          AutosTheme {
            ResultSearchTextField(
              query = "${Account.sample}",
              resultsLoadable =
                ListLoadable.Populated(serializableListOf(ProfileSearchResult.sample))
            )
          }
        }
      }
      .onNode(isResultCard())
      .assertIsDisplayed()
  }
}
