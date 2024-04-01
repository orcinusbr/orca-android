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

package br.com.orcinus.orca.platform.autos.kit.scaffold.bar.navigation.view.text

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.viewinterop.AndroidView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import kotlin.test.Test
import org.junit.Rule

internal class HasTextColorsMatcherTests {
  @get:Rule val composeRule = createComposeRule()

  @Test(expected = AssertionError::class)
  fun doesNotMatchNonTextView() {
    composeRule.setContent { AndroidView(::ImageView) }
    onView(isAssignableFrom(ImageView::class.java))
      .check(matches(hasTextColors(ColorStateList.valueOf(Color.TRANSPARENT))))
  }

  @Test(expected = AssertionError::class)
  fun doesNotMatchTextViewWhoseTextColorsAreNotTheExpectedOnes() {
    val id = View.generateViewId()
    composeRule.setContent {
      AndroidView(::TextView) {
        it.id = id
        it.setTextColor(Color.BLUE)
      }
    }
    onView(withId(id)).check(matches(hasTextColors(ColorStateList.valueOf(Color.GRAY))))
  }

  @Test
  fun matchesTextViewWhoseTextColorsAreTheExpectedOnes() {
    val id = View.generateViewId()
    composeRule.setContent {
      AndroidView(::TextView) {
        it.id = id
        it.setTextColor(ColorStateList.valueOf(Color.TRANSPARENT))
      }
    }
    onView(withId(id)).check(matches(hasTextColors(ColorStateList.valueOf(Color.TRANSPARENT))))
  }
}
