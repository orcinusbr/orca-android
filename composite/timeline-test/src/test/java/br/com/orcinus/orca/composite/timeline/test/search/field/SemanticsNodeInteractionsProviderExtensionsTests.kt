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

import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import br.com.orcinus.orca.composite.timeline.search.field.ResultSearchTextField
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearchResult
import br.com.orcinus.orca.core.sample.feed.profile.account.sample
import br.com.orcinus.orca.core.sample.feed.profile.post.createSamples
import br.com.orcinus.orca.platform.autos.test.isDisplayed
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.core.image.sample
import br.com.orcinus.orca.platform.core.sample
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.loadable.list.serializableListOf
import com.jeanbarrossilva.loadable.list.toSerializableList
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class SemanticsNodeInteractionsProviderExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun findsDismissButton() {
    composeRule
      .apply { setContent { AutosTheme { ResultSearchTextField() } } }
      .onDismissButton()
      .assertIsDisplayed()
  }

  @Test
  fun findsResultCard() {
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
      .onResultCard()
      .assertIsDisplayed()
  }

  @Test
  fun findsResultCards() {
    composeRule
      .apply {
        setContent {
          AutosTheme {
            ResultSearchTextField(
              resultsLoadable =
                ListLoadable.Populated(
                  Author.createSamples(ComposableImageLoader.Provider.sample)
                    .map {
                      ProfileSearchResult(
                        it.id,
                        it.account,
                        it.avatarLoader,
                        it.name,
                        it.profileURI
                      )
                    }
                    .toSerializableList()
                )
            )
          }
        }
      }
      .onResultCards()
      .assertAll(isDisplayed())
  }
}
