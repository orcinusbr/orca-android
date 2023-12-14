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

package com.jeanbarrossilva.orca.feature.gallery

import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.jeanbarrossilva.orca.feature.gallery.test.activity.TestGalleryModule
import com.jeanbarrossilva.orca.feature.gallery.test.launchGalleryActivity
import com.jeanbarrossilva.orca.feature.gallery.test.onPager
import com.jeanbarrossilva.orca.feature.gallery.test.performScrollToEachPage
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.formatted
import com.jeanbarrossilva.orca.std.injector.module.binding.boundTo
import com.jeanbarrossilva.orca.std.injector.test.InjectorTestRule
import org.junit.Rule
import org.junit.Test

internal class GalleryFragmentTests {
  @get:Rule
  val injectorRule = InjectorTestRule { register(TestGalleryModule.boundTo<GalleryModule, _>()) }

  @get:Rule val composeRule = createEmptyComposeRule()

  @Test
  fun describesEachPage() {
    val context = InstrumentationRegistry.getInstrumentation().context
    launchGalleryActivity().use {
      with(composeRule) {
        onPager().performScrollToEachPage {
          assertContentDescriptionEquals(
            context.getString(R.string.feature_gallery_attachment, it.formatted)
          )
        }
      }
    }
  }
}
