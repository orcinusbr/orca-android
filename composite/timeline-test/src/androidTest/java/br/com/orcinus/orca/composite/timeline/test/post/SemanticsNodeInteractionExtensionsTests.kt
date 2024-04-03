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

package br.com.orcinus.orca.composite.timeline.test.post

import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.junit4.createComposeRule
import br.com.orcinus.orca.autos.colors.Colors
import br.com.orcinus.orca.composite.timeline.Timeline
import br.com.orcinus.orca.composite.timeline.post.PostPreview
import br.com.orcinus.orca.composite.timeline.test.onTimeline
import br.com.orcinus.orca.core.sample.feed.profile.post.Posts
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.core.withSamples
import org.junit.Rule
import org.junit.Test

internal class SemanticsNodeInteractionExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun performsScrollToPostIndex() {
    lateinit var colors: Colors
    composeRule
      .apply {
        setContent {
          AutosTheme {
            AutosTheme.colors.let {
              DisposableEffect(Unit) {
                colors = it
                onDispose {}
              }
            }

            Timeline(
              PostPreview.samples,
              onFavorite = {},
              onRepost = {},
              onShare = {},
              onClick = {},
              onNext = {}
            )
          }
        }
      }
      .run {
        onTimeline().performScrollToPostIndex({ it == Posts.withSamples.last() })
        onPostPreviews().assertAny(isPostPreview(PostPreview.getSamples(colors).last()))
      }
  }
}
