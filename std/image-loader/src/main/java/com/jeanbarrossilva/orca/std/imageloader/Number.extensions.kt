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

package com.jeanbarrossilva.orca.std.imageloader

/**
 * Returns [max] if this [Int] is less than [min] or [min] if this is greater than [max]; otherwise,
 * this [Int] itself is returned.
 *
 * @param min Minimum [Int] to be returned if this one exceeds [max].
 * @param max Maximum [Int] to be returned if this one is less than [min].
 */
internal fun Int.mirror(min: Int, max: Int): Int {
  return if (this < min) max else if (this > max) min else this
}
