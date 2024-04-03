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

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import br.com.orcinus.orca.ext.reflection.access
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

/**
 * [Matcher] that matches an [ImageView] whose [Drawable] is the one to which the [resource] is a
 * reference.
 *
 * @param context [Context] through which the [Drawable] will be obtained.
 * @param resource Resource of the [Drawable] that the [ImageView] is expected to contain.
 * @param transform Changes the [Drawable] to the state that it should be.
 * @see ImageView.getDrawable
 */
private class WithDrawableMatcher(
  private val context: Context,
  @DrawableRes private val resource: Int,
  private val transform: (Drawable) -> Unit
) : BaseMatcher<View>() {
  /** [Drawable] with the [resource] that has been found. */
  private var drawable: Drawable? = null

  override fun describeTo(description: Description?) {
    description
      ?.appendText("view.getDrawable() to match resource ID ")
      ?.appendValue(resource)
      ?.appendText("[")
      ?.appendValue(
        runCatching { context.resources?.getResourceEntryName(resource) }
          .onSuccess { drawable = ContextCompat.getDrawable(context, resource)?.apply(transform) }
          .recover { if (it is Resources.NotFoundException) null else throw it }
          .getOrThrow()
      )
      ?.appendText("]")
  }

  @RequiresApi(Build.VERSION_CODES.Q)
  override fun matches(item: Any?): Boolean {
    return item is ImageView &&
      (((resource == Resources.ID_NULL && item.drawable == null) ||
        ImageView::class
          .declaredMemberProperties
          .filterIsInstance<KProperty1<ImageView, Int>>()
          .single { it.name == "mResource" }
          .access { get(item) } == resource ||
        drawable?.constantState == item.drawable?.constantState ||
        drawable?.toBitmap()?.sameAs(item.drawable?.toBitmap()) ?: false))
  }
}

/**
 * [Matcher] that matches an [ImageView] whose [Drawable] is the one to which the [resource] is a
 * reference.
 *
 * @param context [Context] from which the [Drawable] will be obtained.
 * @param resource Resource of the [Drawable] that the [ImageView] is expected to contain.
 * @param transform Changes the [Drawable] to the state that it should be.
 * @see ImageView.getDrawable
 */
fun withDrawable(
  context: Context,
  @DrawableRes resource: Int,
  transform: (Drawable) -> Unit = {}
): Matcher<View> {
  return WithDrawableMatcher(context, resource, transform)
}
