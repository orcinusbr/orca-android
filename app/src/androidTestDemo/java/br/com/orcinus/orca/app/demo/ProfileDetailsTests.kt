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

package br.com.orcinus.orca.app.demo

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import br.com.orcinus.orca.app.R
import br.com.orcinus.orca.app.demo.activity.NonDependencyDejectingOrcaActivity
import br.com.orcinus.orca.composite.timeline.test.isTimeline
import br.com.orcinus.orca.composite.timeline.test.post.figure.link.onLinkCards
import br.com.orcinus.orca.composite.timeline.test.post.performScrollToPostPreviewWithLinkCard
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.platform.core.image.sample
import br.com.orcinus.orca.platform.core.sample
import br.com.orcinus.orca.platform.intents.test.intendBrowsingTo
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader
import kotlin.test.BeforeTest
import org.junit.Rule
import org.junit.Test

internal class ProfileDetailsTests {
  @get:Rule val composeRule = createAndroidComposeRule<NonDependencyDejectingOrcaActivity>()

  @BeforeTest
  fun setUp() {
    onView(withId(R.id.profile_details)).perform(click())
  }

  @Test
  fun navigatesToPostLink() {
    val instance =
      SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
        .withDefaultProfiles()
        .withDefaultPosts()
        .build()
    intendBrowsingTo(
      instance.postProvider
        .provideAllCurrentBy(Actor.Authenticated.sample.id)
        .firstNotNullOf { it.content.highlight }
        .uri
    ) {
      composeRule
        .onAllNodes(isTimeline())
        .onLast()
        .performScrollToPostPreviewWithLinkCard(instance.feedProvider)
      composeRule.onLinkCards().onFirst().performClick()
    }
  }
}
