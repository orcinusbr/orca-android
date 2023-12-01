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

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.button.placement

import androidx.compose.ui.layout.Placeable

/**
 * Indicates where the [placeable] will be placed in relation to previously placed ones.
 *
 * @param placeable [Placeable] yet to be placed.
 * @param axisOffset Additional horizontal or vertical shift in pixels.
 */
internal data class Placement(val placeable: Placeable, val axisOffset: Int)
