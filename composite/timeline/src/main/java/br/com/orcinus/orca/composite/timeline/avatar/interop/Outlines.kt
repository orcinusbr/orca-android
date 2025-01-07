/*
 * Copyright © 2025 Orcinus
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

package br.com.orcinus.orca.composite.timeline.avatar.interop

import android.content.Context
import android.graphics.Outline
import android.graphics.Path
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import br.com.orcinus.orca.autos.forms.Form
import br.com.orcinus.orca.composite.timeline.Units

/**
 * Converts this [Form] into a provider of an [Outline] for a [View].
 *
 * Note that uneven [Form]s are only supported on API level >= 30 (R); thus, in case this one is not
 * uniform — that is, its corners' radius are not all equal to each other — and the OS version is
 * older than that, nothing is done other than the setting of the [Rect].
 *
 * @see Outline.setRect
 */
internal fun Form.PerCorner.asViewOutlineProvider() =
  object : ViewOutlineProvider() {
    override fun getOutline(view: View?, outline: Outline?) {
      if (view != null && outline != null) {
        outline.set(
          view.context,
          view.left,
          view.top,
          view.right,
          view.bottom,
          this@asViewOutlineProvider
        )
      }
    }
  }

/**
 * Applies the given coordinates, dimensions and radii to this [Outline].
 *
 * Note that uneven [Form]s are only supported on API level >= 30 (R); thus, in case the given one
 * is not uniform — that is, its corners' radius are not all equal to each other — and the OS
 * version is older than that, nothing is done other than the setting of the [Rect].
 *
 * @param context [Context] from which [form]'s radii in density-dependent pixels (DPs) are
 *   converted into absolute pixels when it is uneven and distinct rounding for each corner is
 *   supported by the current version of the system.
 * @param left Starting point in the X-axis.
 * @param top Starting point in the Y-axis.
 * @param right End point in the X-axis.
 * @param bottom End point in the Y-axis.
 * @param form [Form] whose radii will be applied to this [Outline].
 * @see Outline.setRect
 */
private fun Outline.set(
  context: Context,
  left: Int,
  top: Int,
  right: Int,
  bottom: Int,
  form: Form.PerCorner
) {
  if (
    form.topStart == form.topEnd &&
      form.topStart == form.bottomEnd &&
      form.topStart == form.bottomStart &&
      form.topEnd == form.bottomEnd &&
      form.topEnd == form.bottomStart
  ) {
    val radius = Units.dp(context, form.topStart.toFloat()).toFloat()
    setRoundRect(left, top, right, bottom, radius)
  } else {
    setRect(left, top, right, bottom)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      val topStartRadiusInPx = Units.dp(context, form.topStart.toFloat()).toFloat()
      val topEndRadiusInPx = Units.dp(context, form.topEnd.toFloat()).toFloat()
      val bottomEndRadiusInPx = Units.dp(context, form.bottomEnd.toFloat()).toFloat()
      val bottomStartRadiusInPx = Units.dp(context, form.bottomStart.toFloat()).toFloat()
      setPath(
        Path().apply {
          addRoundRect(
            left.toFloat(),
            top.toFloat(),
            right.toFloat(),
            bottom.toFloat(),
            /* radii = */ floatArrayOf(
              topStartRadiusInPx,
              topStartRadiusInPx,
              topEndRadiusInPx,
              topEndRadiusInPx,
              bottomEndRadiusInPx,
              bottomEndRadiusInPx,
              bottomStartRadiusInPx,
              bottomStartRadiusInPx
            ),
            Path.Direction.CW
          )
        }
      )
    }
  }
}
