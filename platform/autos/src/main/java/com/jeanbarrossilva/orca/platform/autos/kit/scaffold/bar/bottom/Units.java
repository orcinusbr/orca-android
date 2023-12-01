/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.bottom;

import android.content.Context;
import android.util.TypedValue;
import androidx.annotation.NonNull;

/** Utilities for performing operations with units. */
class Units {
  private Units() {}

  /**
   * Converts the DPs into pixels.
   *
   * @param context {@link Context} through which the conversion will take place.
   * @param dp Amount in DPs to be converted into pixels.
   */
  static int dp(@NonNull Context context, int dp) {
    return (int)
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
  }
}
