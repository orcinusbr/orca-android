/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.feature.feed

import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.jeanbarrossilva.loadable.list.toListLoadable
import com.jeanbarrossilva.loadable.list.toSerializableList
import com.jeanbarrossilva.orca.composite.timeline.post.PostPreview
import com.jeanbarrossilva.orca.composite.timeline.stat.activateable.favorite.FAVORITE_STAT_TAG
import com.jeanbarrossilva.orca.composite.timeline.test.post.onPostPreviews
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

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
      .filterToOne(hasTestTag(FAVORITE_STAT_TAG))
      .performScrollTo()
      .performClick()
    assertTrue(hasCallbackBeenRun)
  }
}
