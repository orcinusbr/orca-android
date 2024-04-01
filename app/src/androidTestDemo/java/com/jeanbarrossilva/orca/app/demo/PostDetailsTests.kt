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

package com.jeanbarrossilva.orca.app.demo

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import com.jeanbarrossilva.orca.app.demo.test.performScrollToPostPreviewWithLinkCard
import com.jeanbarrossilva.orca.composite.timeline.test.post.figure.link.onLinkCards
import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.content.highlight.sample
import com.jeanbarrossilva.orca.platform.intents.test.intendBrowsingTo
import org.junit.Rule
import org.junit.Test

internal class PostDetailsTests {
  @get:Rule val composeRule = createAndroidComposeRule<DemoOrcaActivity>()

  @Test
  fun navigatesToPostLink() {
    intendBrowsingTo("${Highlight.sample.url}") {
      composeRule.performScrollToPostPreviewWithLinkCard()
      composeRule.onLinkCards().onFirst().performClick()
    }
  }
}
