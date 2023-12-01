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

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection

/** End padding calculated through the [LocalLayoutDirection]. */
private val PaddingValues.end
  @Composable get() = calculateEndPadding(LocalLayoutDirection.current)

/** Start padding calculated through the [LocalLayoutDirection]. */
private val PaddingValues.start
  @Composable get() = calculateStartPadding(LocalLayoutDirection.current)

/**
 * Adds the [PaddingValues].
 *
 * @param other [PaddingValues] to add to the receiver one.
 */
@Composable
operator fun PaddingValues.plus(other: PaddingValues): PaddingValues {
  return PaddingValues(
    start + other.start,
    calculateTopPadding() + other.calculateTopPadding(),
    end + other.end,
    calculateBottomPadding() + other.calculateBottomPadding()
  )
}
