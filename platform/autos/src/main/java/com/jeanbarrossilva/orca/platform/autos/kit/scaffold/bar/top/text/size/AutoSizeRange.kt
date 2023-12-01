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

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.text.size

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.text.AutoSizeText

/**
 * Range within which an [AutoSizeText] should be sized.
 *
 * @param density [Density] through which sizes will be converted from [SP][TextUnitType.Sp] to
 *   [Float] and vice-versa.
 * @param min Minimum size.
 * @param max Maximum, default size.
 */
@Immutable
data class AutoSizeRange
internal constructor(
  private val density: Density,
  private val min: TextUnit,
  private val max: TextUnit
) : ClosedFloatingPointRange<Float> {
  override val start = with(density) { min.toPx() }
  override val endInclusive = with(density) { max.toPx() }

  init {
    require(min.type == max.type) {
      "Both minimum and maximum sizes should have the same TextUnitType."
    }
  }

  override fun lessThanOrEquals(a: Float, b: Float): Boolean {
    return with(density) { a.toSp() <= b.toSp() }
  }
}
