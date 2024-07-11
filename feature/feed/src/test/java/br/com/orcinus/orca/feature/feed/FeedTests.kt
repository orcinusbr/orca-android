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

package br.com.orcinus.orca.feature.feed

import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import br.com.orcinus.orca.composite.timeline.post.PostPreview
import br.com.orcinus.orca.composite.timeline.stat.activateable.favorite.FavoriteStatTag
import br.com.orcinus.orca.composite.timeline.test.post.onPostPreviews
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.loadable.list.toListLoadable
import com.jeanbarrossilva.loadable.list.toSerializableList
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class FeedTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun runsCallbackWhenClickingPostFavoriteStat() {
    var hasCallbackBeenRun = false
    composeRule.setContent {
      AutosTheme {
        Feed(
          PostPreview.samples.toSerializableList().toListLoadable(),
          onFavorite = { hasCallbackBeenRun = true }
        )
      }
    }
    composeRule
      .onPostPreviews()
      .onFirst()
      .onChildren()
      .filterToOne(hasTestTag(FavoriteStatTag))
      .performScrollTo()
      .performClick()
    assertTrue(hasCallbackBeenRun)
  }
}
