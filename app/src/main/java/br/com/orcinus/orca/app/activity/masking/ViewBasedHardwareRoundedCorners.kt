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

package br.com.orcinus.orca.app.activity.masking

import android.os.Build
import android.view.RoundedCorner
import android.view.View
import androidx.annotation.RequiresApi

/**
 * [HardwareRoundedCorners] that provides radii through the [view].
 *
 * @param view [View] from which information about the corners of the display are retrieved.
 */
internal class ViewBasedHardwareRoundedCorners(private val view: View) : HardwareRoundedCorners {
  @RequiresApi(Build.VERSION_CODES.S)
  override fun bottomRight(): Float {
    return getRadiusOrNaN(RoundedCorner.POSITION_BOTTOM_RIGHT)
  }

  @RequiresApi(Build.VERSION_CODES.S)
  override fun bottomLeft(): Float {
    return getRadiusOrNaN(RoundedCorner.POSITION_BOTTOM_LEFT)
  }

  /**
   * Obtains the radius of the specified corner of the display to which the [view] is attached. In
   * case it is not attached to one (due to its non-visual context or its detachment from a window),
   * there are no rounded corners or a value cannot be retrieved from the current API level, a NaN
   * is returned.
   *
   * @param position [RoundedCorner] constant representing the position at which the radius to be
   *   obtained is.
   */
  private fun getRadiusOrNaN(position: Int): Float {
    val display = view.display
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && display != null) {
      view.display!!.getRoundedCorner(position)?.radius?.toFloat()
    } else {
      null
    }
      ?: Float.NaN
  }
}
