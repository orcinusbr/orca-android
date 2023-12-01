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

package com.jeanbarrossilva.orca.platform.autos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Shape
import com.jeanbarrossilva.orca.autos.borders.Borders
import com.jeanbarrossilva.orca.platform.autos.borders.areApplicable
import com.jeanbarrossilva.orca.platform.autos.borders.asBorderStroke
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme

/**
 * Applies a [BorderStroke], shaped by the given [shape], when they are applicable.
 *
 * @param shape [Shape] of the [BorderStroke] to be applied.
 * @see Borders.areApplicable
 */
fun Modifier.border(shape: Shape): Modifier {
  return composed {
    if (Borders.areApplicable) border(AutosTheme.borders.default.asBorderStroke, shape) else this
  }
}
