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

package br.com.orcinus.orca.feature.gallery

import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import br.com.orcinus.orca.composite.timeline.stat.details.formatted
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.feature.gallery.test.UnnavigableGalleryModule
import br.com.orcinus.orca.feature.gallery.test.launchGalleryActivity
import br.com.orcinus.orca.feature.gallery.test.ui.onPager
import br.com.orcinus.orca.feature.gallery.test.ui.performScrollToEachPage
import br.com.orcinus.orca.platform.core.image.sample
import br.com.orcinus.orca.platform.testing.asString
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader
import br.com.orcinus.orca.std.injector.module.binding.boundTo
import br.com.orcinus.orca.std.injector.test.InjectorTestRule
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class GalleryActivityTests {
  private val postProvider =
    SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
      .withDefaultProfiles()
      .withDefaultPosts()
      .build()
      .postProvider

  @get:Rule
  val injectorRule = InjectorTestRule {
    register(UnnavigableGalleryModule(postProvider).boundTo<GalleryModule, _>())
  }

  @get:Rule val composeRule = createEmptyComposeRule()

  @Test
  fun describesEachPage() {
    launchGalleryActivity(postProvider).use {
      with(composeRule) {
        onPager().performScrollToEachPage {
          assertContentDescriptionEquals(R.string.feature_gallery_attachment.asString(it.formatted))
        }
      }
    }
  }
}
