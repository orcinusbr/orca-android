/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.platform.autos.kit;

import android.content.Context;
import android.util.TypedValue;
import androidx.annotation.NonNull;

/** Utilities for performing operations with units. */
public class Units {
  private Units() {}

  /**
   * Converts the DPs into pixels.
   *
   * @param context {@link Context} through which the conversion will take place.
   * @param dp Amount in DPs to be converted into pixels.
   */
  public static int dp(@NonNull Context context, float dp) {
    return (int)
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
  }
}
