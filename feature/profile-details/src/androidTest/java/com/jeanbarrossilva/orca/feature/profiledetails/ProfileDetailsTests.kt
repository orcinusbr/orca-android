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

package com.jeanbarrossilva.orca.feature.profiledetails

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollToIndex
import androidx.test.platform.app.InstrumentationRegistry
import com.jeanbarrossilva.loadable.list.toListLoadable
import com.jeanbarrossilva.loadable.list.toSerializableList
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.component.timeline.TIMELINE_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.PostPreview
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.onTimeline
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.time.TestRelativeTimeProvider
import java.util.UUID
import org.junit.Rule
import org.junit.Test

internal class ProfileDetailsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun showsUsernameWhenScrollingPastHeader() {
    val screenHeightInPx =
      InstrumentationRegistry.getInstrumentation().context.resources.displayMetrics.heightPixels
    composeRule.setContent {
      AutosTheme {
        ProfileDetails(
          postPreviewsLoadable =
            List(size = screenHeightInPx) { PostPreview.sample.copy(id = "${UUID.randomUUID()}") }
              .toSerializableList()
              .toListLoadable(),
          relativeTimeProvider = TestRelativeTimeProvider
        )
      }
    }
    composeRule.onTimeline().performScrollToIndex(1)
    composeRule.onNodeWithTag(TIMELINE_TAG).assertIsDisplayed()
  }
}
