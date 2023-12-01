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

package com.jeanbarrossilva.orca.platform.autos.forms

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.autos.forms.Form

/** [Shape] version of this [Form]. */
val Form.asShape: CornerBasedShape
  get() =
    when (this) {
      is Form.PerCorner -> RoundedCornerShape(topStart.dp, topEnd.dp, bottomEnd.dp, bottomStart.dp)
      is Form.Percent -> RoundedCornerShape(percentage * 100)
    }
