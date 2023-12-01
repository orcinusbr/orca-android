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

package com.jeanbarrossilva.orca.std.styledstring.style

import java.io.Serializable

/** Indicates where and how a target has been stylized. */
interface Style : Serializable {
  /** Indices at which both the style symbols and the target are in the whole [String]. */
  val indices: IntRange

  /** [Style] that requires its targets to match the [regex]. */
  abstract class Constrained : Style {
    /** [Regex] that targets to which this [Style] is applied should match. */
    internal abstract val regex: Regex

    /**
     * [IllegalArgumentException] thrown if the target is an invalid one for this [Style].
     *
     * @param target Target that's invalid.
     */
    inner class InvalidTargetException internal constructor(target: String) :
      IllegalArgumentException("Target doesn't match regex ($regex): \"$target\".")
  }

  /**
   * Copies this [Style].
   *
   * @param indices Indices at which both the style symbols and the target are in the whole
   *   [String].
   */
  fun at(indices: IntRange): Style
}
