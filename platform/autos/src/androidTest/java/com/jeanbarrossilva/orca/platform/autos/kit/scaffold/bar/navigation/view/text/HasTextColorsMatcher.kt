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

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.navigation.view.text

import android.content.res.ColorStateList
import android.view.View
import android.widget.TextView
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

/**
 * [Matcher] that matches a [TextView] whose text is colored by the given colors.
 *
 * @param colors [ColorStateList] containing the colors by which the text is expected to be colored.
 * @see TextView.getText
 * @see TextView.getTextColors
 */
private class HasTextColorsMatcher(private val colors: ColorStateList) : BaseMatcher<View>() {
  override fun describeTo(description: Description?) {
    description?.appendText("view.getTextColors() to be ")?.appendValue(colors)
  }

  override fun matches(item: Any?): Boolean {
    return item is TextView && item.textColors == colors
  }
}

/**
 * [Matcher] that matches a [TextView] whose text is colored by the given colors.
 *
 * @param colors [ColorStateList] containing the colors by which the text is expected to be colored.
 * @see TextView.getText
 * @see TextView.getTextColors
 */
internal fun hasTextColors(colors: ColorStateList): Matcher<View> {
  return HasTextColorsMatcher(colors)
}
