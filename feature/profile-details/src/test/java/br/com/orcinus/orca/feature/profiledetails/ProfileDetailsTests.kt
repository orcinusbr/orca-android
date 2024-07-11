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

package br.com.orcinus.orca.feature.profiledetails

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollToIndex
import br.com.orcinus.orca.composite.timeline.TimelineTag
import br.com.orcinus.orca.composite.timeline.post.PostPreview
import br.com.orcinus.orca.composite.timeline.test.onTimeline
import br.com.orcinus.orca.composite.timeline.test.post.time.StringRelativeTimeProvider
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.testing.screen.screen
import com.jeanbarrossilva.loadable.list.toListLoadable
import com.jeanbarrossilva.loadable.list.toSerializableList
import java.util.UUID
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ProfileDetailsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun showsUsernameWhenScrollingPastHeader() {
    composeRule.setContent {
      AutosTheme {
        ProfileDetails(
          postPreviewsLoadable =
            List(size = screen.height.inPixels) {
                PostPreview.sample.copy(id = "${UUID.randomUUID()}")
              }
              .toSerializableList()
              .toListLoadable(),
          relativeTimeProvider = StringRelativeTimeProvider
        )
      }
    }
    composeRule.onTimeline().performScrollToIndex(1)
    composeRule.onNodeWithTag(TimelineTag).assertIsDisplayed()
  }
}
