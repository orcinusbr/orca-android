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

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.navigation.view.image

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

/**
 * [Matcher] that matches an [ImageView] whose image's tint equals to the given one.
 *
 * @param tint [ColorStateList] by which the image of the [ImageView] is expected to be tinted.
 * @see ImageView.getImageTintList
 */
private class WithImageTintMatcher(private val tint: ColorStateList) : BaseMatcher<View>() {
  override fun describeTo(description: Description?) {
    description?.appendText("view.getImageTintList() to be ")?.appendValue(tint)
  }

  override fun matches(item: Any?): Boolean {
    return item is ImageView && item.imageTintList == tint
  }
}

/**
 * [Matcher] that matches an [ImageView] whose image's tint equals to the given one.
 *
 * @param tint [ColorStateList] by which the image of the [ImageView] is expected to be tinted.
 * @see ImageView.getImageTintList
 */
internal fun withImageTint(tint: ColorStateList): Matcher<View> {
  return WithImageTintMatcher(tint)
}
