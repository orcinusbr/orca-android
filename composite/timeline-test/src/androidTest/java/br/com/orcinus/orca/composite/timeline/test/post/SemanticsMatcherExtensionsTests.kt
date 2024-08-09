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

package br.com.orcinus.orca.composite.timeline.test.post

import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import br.com.orcinus.orca.autos.colors.Colors
import br.com.orcinus.orca.composite.timeline.post.LoadedPostPreview
import br.com.orcinus.orca.composite.timeline.post.PostPreview
import br.com.orcinus.orca.composite.timeline.test.post.time.StringRelativeTimeProvider
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.core.image.sample
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader
import org.junit.Rule
import org.junit.Test

internal class SemanticsMatcherExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun matchesPostPreviewNode() {
    composeRule.setContent { AutosTheme { PostPreview() } }
    composeRule.onNode(isPostPreview()).assertIsDisplayed()
  }

  @Test
  fun matchesLinkedPostPreviewNode() {
    val postProvider =
      SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
        .withDefaultProfiles()
        .withDefaultPosts()
        .build()
        .postProvider
    lateinit var colors: Colors
    composeRule.setContent {
      AutosTheme {
        AutosTheme.colors.let {
          DisposableEffect(Unit) {
            colors = it
            onDispose {}
          }
        }

        LoadedPostPreview(
          PostPreview.createSample(postProvider),
          relativeTimeProvider = StringRelativeTimeProvider
        )
      }
    }
    composeRule
      .onPostPreview()
      .assert(isPostPreview(PostPreview.createSample(postProvider, colors)))
  }
}
