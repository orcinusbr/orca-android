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

package com.jeanbarrossilva.orca.std.injector.processor.inject

import com.google.devtools.ksp.symbol.Visibility

/**
 * Returns the [Visibility] that's least visible.
 *
 * @param a [Visibility] to be compared to [b].
 * @param b [Visibility] to be compared to [a].
 */
internal fun minOf(a: Visibility, b: Visibility): Visibility {
  val aWeight = a.weight()
  val bWeight = b.weight()
  val minWeight = listOf(aWeight, bWeight).min()
  return if (minWeight == aWeight) a else b
}

/**
 * Since the [Visibility] enum entries aren't sorted according to their "weight", returns a positive
 * [Int] that classifies this [Visibility].
 */
private fun Visibility.weight(): Int {
  return when (this) {
    Visibility.LOCAL -> 0
    Visibility.PRIVATE -> 1
    Visibility.PROTECTED -> 2
    Visibility.JAVA_PACKAGE -> 3
    Visibility.INTERNAL -> 4
    Visibility.PUBLIC -> 5
  }
}
