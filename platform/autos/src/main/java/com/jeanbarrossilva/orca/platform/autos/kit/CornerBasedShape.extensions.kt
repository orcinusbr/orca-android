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

package com.jeanbarrossilva.orca.platform.autos.kit

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.ZeroCornerSize

/** Version of this [CornerBasedShape] with zeroed top [CornerSize]s. */
internal val CornerBasedShape.bottom
  get() = copy(topStart = ZeroCornerSize, topEnd = ZeroCornerSize)

/** Version of this [CornerBasedShape] with zeroed bottom [CornerSize]s. */
internal val CornerBasedShape.top
  get() = copy(bottomEnd = ZeroCornerSize, bottomStart = ZeroCornerSize)
