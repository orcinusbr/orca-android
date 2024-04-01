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
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import assertk.assertThat
import com.jeanbarrossilva.orca.app.R
import com.jeanbarrossilva.orca.app.demo.test.performScrollToPostPreviewWithLinkCard
import com.jeanbarrossilva.orca.app.demo.test.performStartClick
import com.jeanbarrossilva.orca.composite.timeline.test.post.figure.link.onLinkCards
import com.jeanbarrossilva.orca.composite.timeline.test.post.onPostPreviews
import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.Posts
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.content.highlight.sample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.withSamples
import com.jeanbarrossilva.orca.feature.postdetails.PostDetailsFragment
import com.jeanbarrossilva.orca.platform.intents.test.intendBrowsingTo
import com.jeanbarrossilva.orca.platform.navigation.test.isAt
import org.junit.Rule
import org.junit.Test

internal class ProfileDetailsTests {
  @get:Rule val composeRule = createAndroidComposeRule<DemoOrcaActivity>()

  @Test
  fun navigatesToPostLink() {
    intendBrowsingTo("${Highlight.sample.url}") {
      composeRule.performScrollToPostPreviewWithLinkCard()
      composeRule.onLinkCards().onFirst().performClick()
    }
  }

  @Test
  fun navigatesToPostDetailsOnPostPreviewClick() {
    onView(withId(R.id.profile_details)).perform(click())
    composeRule.onPostPreviews().onFirst().performStartClick()
    assertThat(composeRule.activity)
      .isAt(PostDetailsFragment.getRoute(Posts.withSamples.first().id))
  }
}
