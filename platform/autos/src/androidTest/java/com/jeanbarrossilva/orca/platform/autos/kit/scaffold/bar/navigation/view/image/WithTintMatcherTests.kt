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

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.navigation.view.image

import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.ImageButton
import android.widget.ImageView
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.viewinterop.AndroidView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import kotlin.test.Test
import org.junit.Rule

internal class WithTintMatcherTests {
  @get:Rule val composeRule = createComposeRule()

  @Test(expected = AssertionError::class)
  fun doesNotMatchNonImageView() {
    composeRule.setContent { AndroidView(::ImageButton) }
    onView(isAssignableFrom(ImageButton::class.java))
      .check(matches(withImageTint(ColorStateList.valueOf(Color.TRANSPARENT))))
  }

  @Test(expected = AssertionError::class)
  fun doesNotMatchImageViewWithoutExpectedImageTint() {
    composeRule.setContent {
      AndroidView(::ImageView) { it.imageTintList = ColorStateList.valueOf(Color.BLACK) }
    }
    onView(isAssignableFrom(ImageView::class.java))
      .check(matches(withImageTint(ColorStateList.valueOf(Color.TRANSPARENT))))
  }

  @Test
  fun matchesImageViewWithExpectedImageTint() {
    composeRule.setContent {
      AndroidView(::ImageView) { it.imageTintList = ColorStateList.valueOf(Color.TRANSPARENT) }
    }
    onView(isAssignableFrom(ImageView::class.java))
      .check(matches(withImageTint(ColorStateList.valueOf(Color.TRANSPARENT))))
  }
}
