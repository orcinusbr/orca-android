/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.app.demo

import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.junit4.createComposeRule
import com.jeanbarrossilva.orca.app.demo.test.isPostPreview
import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.composite.timeline.post.LoadedPostPreview
import com.jeanbarrossilva.orca.composite.timeline.post.PostPreview
import com.jeanbarrossilva.orca.composite.timeline.test.post.onPostPreview
import com.jeanbarrossilva.orca.composite.timeline.test.post.time.StringRelativeTimeProvider
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import org.junit.Rule
import org.junit.Test

internal class SemanticsMatcherExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun linksPostPreview() {
    lateinit var colors: Colors
    composeRule.setContent {
      AutosTheme {
        AutosTheme.colors.let {
          DisposableEffect(Unit) {
            colors = it
            onDispose {}
          }
        }

        LoadedPostPreview(relativeTimeProvider = StringRelativeTimeProvider)
      }
    }
    composeRule.onPostPreview().assert(isPostPreview(PostPreview.getSample(colors)))
  }
}
