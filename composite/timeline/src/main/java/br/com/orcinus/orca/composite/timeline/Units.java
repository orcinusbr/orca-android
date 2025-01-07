/*
 * Copyright © 2023–2025 Orcinus
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

package br.com.orcinus.orca.composite.timeline;

import android.content.Context;
import android.util.TypedValue;
import androidx.annotation.NonNull;
import androidx.core.util.TypedValueCompat;

/** Utilities for performing operations with units. */
public final class Units {
  private Units() {}

  /**
   * Converts the DPs into pixels.
   *
   * @param context {@link Context} through which the conversion will take place.
   * @param dp Amount in DPs to be converted into pixels.
   */
  public static int dp(@NonNull final Context context, final float dp) {
    return (int)
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
  }

  /**
   * Converts the pixels into DPs.
   *
   * @param context {@link Context} through which the conversion will take place.
   * @param px Amount in pixels to be converted into DPs.
   */
  public static float px(@NonNull final Context context, final int px) {
    return TypedValueCompat.deriveDimension(
        TypedValue.COMPLEX_UNIT_PX, px, context.getResources().getDisplayMetrics());
  }
}
