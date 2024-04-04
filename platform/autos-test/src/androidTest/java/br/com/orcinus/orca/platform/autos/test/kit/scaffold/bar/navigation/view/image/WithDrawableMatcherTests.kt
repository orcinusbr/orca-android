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

import android.widget.ImageView
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.viewinterop.AndroidView
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import br.com.orcinus.orca.platform.autos.R
import br.com.orcinus.orca.platform.testing.context
import kotlin.test.Test
import org.junit.Rule

internal class WithDrawableMatcherTests {
  @get:Rule val composeRule = createComposeRule()

  @Test(expected = AssertionError::class)
  fun doesNotMatchImageViewWithoutExpectedDrawable() {
    composeRule.setContent {
      AndroidView(::ImageView) { it.setImageResource(R.drawable.icon_back) }
    }
    Espresso.onView(ViewMatchers.isAssignableFrom(ImageView::class.java))
      .check(ViewAssertions.matches(withDrawable(context, R.drawable.icon_add)))
  }
}
