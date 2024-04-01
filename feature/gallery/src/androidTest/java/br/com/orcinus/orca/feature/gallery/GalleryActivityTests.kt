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

package br.com.orcinus.orca.feature.gallery

import android.util.TypedValue
import android.view.WindowInsets
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import br.com.orcinus.orca.composite.timeline.stat.details.formatted
import br.com.orcinus.orca.feature.gallery.test.activity.TestGalleryModule
import br.com.orcinus.orca.feature.gallery.test.launchGalleryActivity
import br.com.orcinus.orca.feature.gallery.test.ui.onPager
import br.com.orcinus.orca.feature.gallery.test.ui.performScrollToEachPage
import br.com.orcinus.orca.platform.testing.asString
import br.com.orcinus.orca.platform.testing.context
import br.com.orcinus.orca.platform.testing.screen.screen
import br.com.orcinus.orca.std.injector.module.binding.boundTo
import br.com.orcinus.orca.std.injector.test.InjectorTestRule
import org.junit.Rule
import org.junit.Test

internal class GalleryActivityTests {
  @get:Rule
  val injectorRule = InjectorTestRule { register(TestGalleryModule.boundTo<GalleryModule, _>()) }

  @get:Rule val composeRule = createEmptyComposeRule()

  @Test
  fun isEdgeToEdge() {
    val density = Density(context)
    val resources = context.resources
    var systemBarsHeight = Dp.Unspecified
    val actionBarHeight =
      TypedValue()
        .also { context.theme.resolveAttribute(android.R.attr.actionBarSize, it, true) }
        .let { resources.getDimensionPixelSize(it.resourceId) }
        .let { with(density) { it.toDp() } }
    launchGalleryActivity().use { scenario ->
      scenario.onActivity { activity ->
        activity.windowManager
          ?.currentWindowMetrics
          ?.windowInsets
          ?.getInsets(WindowInsets.Type.systemBars())
          ?.run { systemBarsHeight = with(density) { (top + bottom).toDp() } }
      }
      composeRule
        .onRoot()
        .assertWidthIsEqualTo(screen.width.inDps)
        .assertHeightIsEqualTo(screen.height.inDps + (systemBarsHeight - actionBarHeight))
    }
  }

  @Test
  fun describesEachPage() {
    launchGalleryActivity().use {
      with(composeRule) {
        onPager().performScrollToEachPage {
          assertContentDescriptionEquals(R.string.feature_gallery_attachment.asString(it.formatted))
        }
      }
    }
  }
}
