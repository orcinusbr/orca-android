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

package br.com.orcinus.orca.platform.autos.test.kit.scaffold.bar.navigation.view.image

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.widget.ImageView
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.viewinterop.AndroidView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import br.com.orcinus.orca.platform.autos.R
import br.com.orcinus.orca.platform.testing.context
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class WithDrawableMatcherTests {
  @get:Rule val composeRule = createComposeRule()

  @Test(expected = AssertionError::class)
  fun doesNotMatchImageViewWithoutDrawableWhenOneIsExpected() {
    composeRule.setContent { AndroidView(::ImageView) }
    onView(isAssignableFrom(ImageView::class.java))
      .check(matches(withDrawable(context, R.drawable.icon_add)))
  }

  @Test
  fun matchesImageViewWithoutDrawableWhenNoneIsExpected() {
    composeRule.setContent { AndroidView(::ImageView) }
    onView(isAssignableFrom(ImageView::class.java))
      .check(matches(withDrawable(context, Resources.ID_NULL)))
  }

  @Test
  fun matchesImageViewWithExpectedDrawable() {
    composeRule.setContent {
      AndroidView(::ImageView) { it.setImageDrawable(context.getDrawable(R.drawable.icon_add)) }
    }
    onView(isAssignableFrom(ImageView::class.java))
      .check(matches(withDrawable(context, R.drawable.icon_add)))
  }

  @Test
  fun matchesImageViewWithExpectedDrawableFromIDResource() {
    composeRule.setContent { AndroidView(::ImageView) { it.setImageResource(R.drawable.icon_add) } }
    onView(isAssignableFrom(ImageView::class.java))
      .check(matches(withDrawable(context, R.drawable.icon_add)))
  }

  @Test
  fun transformsDrawable() {
    composeRule.setContent {
      AndroidView(::ImageView) {
        it.setImageResource(R.drawable.icon_add)
        it.imageTintList = ColorStateList.valueOf(Color.GREEN)
      }
    }
    onView(isAssignableFrom(ImageView::class.java))
      .check(matches(withDrawable(context, R.drawable.icon_add) { it.setTint(Color.GREEN) }))
  }
}
