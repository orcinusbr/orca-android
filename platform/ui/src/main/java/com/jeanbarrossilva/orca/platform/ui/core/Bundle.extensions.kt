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

package com.jeanbarrossilva.orca.platform.ui.core

import android.os.Bundle
import androidx.core.os.bundleOf

/**
 * Creates a [Bundle] with the given key-value [pairs] put into it.
 *
 * @param pairs Elements to be put into the resulting [Bundle].
 * @return [Bundle], or `null` if [pairs] is empty.
 */
@PublishedApi
internal fun bundleOf(vararg pairs: Pair<String, Any?>): Bundle? {
  val hasArgs = pairs.isNotEmpty()
  return if (hasArgs) bundleOf(*pairs) else null
}
