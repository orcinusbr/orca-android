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

package br.com.orcinus.orca.platform.autos.kit.input.text.composition

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.setPadding
import br.com.orcinus.orca.autos.Spacings
import br.com.orcinus.orca.platform.autos.R
import br.com.orcinus.orca.platform.autos.kit.Units

/**
 * Text field for composing or editing a post.
 *
 * @param context [Context] in which it will be added.
 * @param attributeSet Attributes specified in XML.
 * @param defaultStyleAttribute Attribute of the style to be applied by default.
 */
class CompositionTextField
@JvmOverloads
constructor(
  context: Context,
  attributeSet: AttributeSet? = null,
  defaultStyleAttribute: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attributeSet, defaultStyleAttribute) {
  /** Delegate by which the [error] is measured and drawn. */
  private val errorDelegate = ErrorDelegate(this)

  init {
    setBackgroundColor(getBackgroundColor(context))
    setPadding(getSpacing(context))
    compoundDrawablePadding = getSpacing(context)
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    errorDelegate.draw(canvas)
  }

  override fun setError(error: CharSequence?, icon: Drawable?) {
    super.setError(error, icon)
    errorDelegate.toggle(error)
  }

  override fun onConfigurationChanged(newConfig: Configuration?) {
    errorDelegate.invalidate(newConfig)
    super.onConfigurationChanged(newConfig)
  }

  companion object {
    /**
     * Obtains the color of a [CompositionTextField]'s background.
     *
     * @param context [Context] with which the color will be resolved from its resource.
     */
    @JvmName("getBackgroundColor")
    @JvmStatic
    @ColorInt
    internal fun getBackgroundColor(context: Context): Int {
      return context.getColor(R.color.surfaceContainer)
    }

    /**
     * Obtains the amount of pixels by which the surroundings are to be padded and drawn components
     * are to be spaced.
     *
     * @param context [Context] with which conversion from DPs to pixels is performed.
     */
    @JvmName("getSpacing")
    @JvmStatic
    @Px
    internal fun getSpacing(context: Context): Int {
      return Units.dp(context, Spacings.default.medium)
    }
  }
}
