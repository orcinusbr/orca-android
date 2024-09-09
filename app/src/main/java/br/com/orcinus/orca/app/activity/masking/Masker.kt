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

import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import androidx.annotation.VisibleForTesting
import br.com.orcinus.orca.autos.forms.Form
import br.com.orcinus.orca.autos.forms.Forms
import br.com.orcinus.orca.std.markdown.style.`if`

/**
 * Masks a specific region of a [MaskableFrameLayout] according to the radii of the corners of the
 * display. The static [mask] method can be called in order to mask all applicable extremities —
 * that is, those which ought to be rounded according to the design specification of Orca.
 *
 * @see HardwareRoundedCorners
 */
enum class Masker {
  /** Masks the trailing corner at the bottom of the [MaskableFrameLayout]. */
  BOTTOM_END {
    override fun getCornerRadiiIndices(layoutDirection: Int): IntArray {
      return if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
        bottomLeftRadiiIndices
      } else {
        bottomRightRadiiIndices
      }
    }

    override fun getRoundedCornerRadius(
      hardwareRoundedCorners: HardwareRoundedCorners,
      layoutDirection: Int
    ): Float {
      return if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
        hardwareRoundedCorners.bottomLeft()
      } else {
        hardwareRoundedCorners.bottomRight()
      }
    }

    override fun getDefaultRadius(displayMetrics: DisplayMetrics): Float {
      return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        defaultForm.bottomEnd,
        displayMetrics
      )
    }
  },

  /** Masks the leading portion at the bottom of the [MaskableFrameLayout]. */
  BOTTOM_START {
    override fun getCornerRadiiIndices(layoutDirection: Int): IntArray {
      return if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
        bottomRightRadiiIndices
      } else {
        bottomLeftRadiiIndices
      }
    }

    override fun getRoundedCornerRadius(
      hardwareRoundedCorners: HardwareRoundedCorners,
      layoutDirection: Int
    ): Float {
      return if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
        hardwareRoundedCorners.bottomRight()
      } else {
        hardwareRoundedCorners.bottomLeft()
      }
    }

    override fun getDefaultRadius(displayMetrics: DisplayMetrics): Float {
      return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        defaultForm.bottomStart,
        displayMetrics
      )
    }
  };

  /**
   * Obtains the indices at which the radii for the equivalent corner are, as specified by a
   * [RoundRectShape]'s outer radii.
   *
   * @param layoutDirection Integer that represents the current direction of the layout.
   *   Left-to-right (LTR) is denoted by [View.LAYOUT_DIRECTION_LTR]; right-to-left (RTL),
   *   [View.LAYOUT_DIRECTION_RTL]. If the actual value is neither of those, this method will apply
   *   the radius it would for LTR.
   */
  protected abstract fun getCornerRadiiIndices(layoutDirection: Int): IntArray

  /**
   * Obtains the radius in pixels of the equivalent corner of the display, defaulting to that of
   * αὐτός' large form in case there are no rounded corners, they are unavailable or the current API
   * level is an unsupported one — that is, < 31 (S).
   *
   * @param view [View] to which the mask is to be applied.
   * @see Forms.large
   */
  protected fun getRadius(hardwareRoundedCorners: HardwareRoundedCorners, view: View): Float {
    return getRoundedCornerRadius(hardwareRoundedCorners, view.layoutDirection).`if`<Float?>({
      this == null || isNaN()
    }) {
      view.context?.resources?.displayMetrics?.let(::getDefaultRadius)
    }
      ?: Float.NaN
  }

  /**
   * Obtains the default radius. For when the display-corner-matching one has been tried to be
   * retrieved but could not due to it not being available or supported by the current system API
   * level.
   *
   * @param displayMetrics Metrics of the display of the device.
   */
  protected abstract fun getDefaultRadius(displayMetrics: DisplayMetrics): Float

  /**
   * Obtains the radius which is equivalent to the radius of the matching corner of the display.
   * Returns NaN when it has no rounded corners, they are unavailable or unsupported.
   *
   * @param hardwareRoundedCorners Provider that allows for retrieving the radius of each applicable
   *   corner of the display.
   * @param layoutDirection Integer that represents the current direction of the layout.
   *   Left-to-right (LTR) is denoted by [View.LAYOUT_DIRECTION_LTR]; right-to-left (RTL),
   *   [View.LAYOUT_DIRECTION_RTL]. If the actual value is neither of those, this method will
   *   default to returning the radius it would for LTR.
   */
  protected abstract fun getRoundedCornerRadius(
    hardwareRoundedCorners: HardwareRoundedCorners,
    layoutDirection: Int
  ): Float

  companion object {
    /**
     * Indices of the radii of a bottom right corner.
     *
     * @see getCornerRadiiIndices
     */
    @JvmStatic private val bottomRightRadiiIndices = intArrayOf(4, 5)

    /**
     * Indices of the radii of a bottom left corner.
     *
     * @see getCornerRadiiIndices
     */
    @JvmStatic private val bottomLeftRadiiIndices = intArrayOf(6, 7)

    /**
     * [Form] by whose applicable regions a [MaskableFrameLayout] is masked by default when the
     * display's rounded corners are nonexistent, unavailable or unsupported.
     */
    @JvmStatic @VisibleForTesting internal val defaultForm = Forms.default.large as Form.PerCorner

    /**
     * Applies to the [view] the radii that are either adapted to the applicable corners of the
     * display or that of αὐτός' large form in case there are no rounded corners, they are
     * unavailable or the current API level is an unsupported one — that is, < 31 (S).
     *
     * @param view [MaskableFrameLayout] to which the radius will be applied.
     */
    @JvmName("maskApplicableCorners")
    @JvmStatic
    internal fun mask(view: MaskableFrameLayout) {
      mask(ViewBasedHardwareRoundedCorners(view), view)
    }

    /**
     * Applies to the [view] the radii that are either adapted to the corners whose radii are
     * retrievable from the given provider or that of αὐτός' large form in case there are no rounded
     * corners, they are unavailable or the current API level is an unsupported one — that is, < 31
     * (S).
     *
     * @param hardwareRoundedCorners Provider that allows for retrieving the radius of each
     *   applicable corner of the display.
     * @param view [MaskableFrameLayout] to which the radius will be applied.
     */
    @JvmName("maskApplicableCorners")
    @JvmStatic
    @VisibleForTesting
    internal fun mask(hardwareRoundedCorners: HardwareRoundedCorners, view: MaskableFrameLayout) {
      val mask = ShapeDrawable()
      val cornerRadii = FloatArray(size = 8) { 0f }
      for (masker in entries) {
        val cornerRadiiIndices = masker.getCornerRadiiIndices(view.layoutDirection)
        val radius = masker.getRadius(hardwareRoundedCorners, view)
        for (cornerRadiusIndex in cornerRadiiIndices) {
          cornerRadii[cornerRadiusIndex] = radius
        }
      }
      mask.shape = RoundRectShape(cornerRadii, null, null)
      view.setMask(mask)
    }
  }
}
