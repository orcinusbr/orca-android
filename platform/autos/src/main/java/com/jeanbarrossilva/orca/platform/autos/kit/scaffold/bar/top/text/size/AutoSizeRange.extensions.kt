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

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme

/**
 * Remembers an [AutoSizeRange], defaulting the [minimum][AutoSizeRange.min] size to
 * [AutosTheme.typography]'s [Typography.labelSmall]'s.
 *
 * @param max Maximum, default size.
 */
@Composable
fun rememberAutoSizeRange(max: TextUnit): AutoSizeRange {
  return rememberAutoSizeRange(min = AutosTheme.typography.labelSmall.fontSize, max)
}

/**
 * Remembers an [AutoSizeRange].
 *
 * @param min Minimum size.
 * @param max Maximum, default size.
 */
@Composable
fun rememberAutoSizeRange(min: TextUnit, max: TextUnit): AutoSizeRange {
  val density = LocalDensity.current
  return remember(density, min, max) { AutoSizeRange(density, min, max) }
}
