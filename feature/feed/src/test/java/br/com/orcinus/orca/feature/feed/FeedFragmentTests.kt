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

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import br.com.orcinus.orca.composite.timeline.stat.activateable.favorite.FavoriteStatTag
import br.com.orcinus.orca.composite.timeline.test.post.onPostPreviews
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.platform.core.image.sample
import br.com.orcinus.orca.platform.core.sample
import br.com.orcinus.orca.platform.navigation.BackStack
import br.com.orcinus.orca.platform.navigation.test.fragment.launchFragmentInNavigationContainer
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader
import br.com.orcinus.orca.std.injector.test.InjectorTestRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class FeedFragmentTests {
  private val instance =
    SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
      .withDefaultProfiles()
      .withDefaultPosts()
      .build()

  @get:Rule
  val injectorRule = InjectorTestRule {
    register<FeedModule>(
      SampleFeedModule(instance.profileSearcher, instance.feedProvider, instance.postProvider)
    )
  }
  @get:Rule val composeRule = createEmptyComposeRule()

  @Test
  fun favoritesPost() {
    runTest {
      instance.feedProvider
        .provide(Actor.Authenticated.sample.id, page = 0)
        .first()
        .first()
        .favorite
        .disable()
    }
    launchFragmentInNavigationContainer {
        FeedFragment(BackStack.named(FeedFragment::class.java.name), Actor.Authenticated.sample.id)
      }
      .use {
        composeRule
          .onPostPreviews()
          .onFirst()
          .onChildren()
          .filterToOne(hasTestTag(FavoriteStatTag))
          .performScrollTo()
          .performClick()
          .assertIsSelected()
      }
  }
}
