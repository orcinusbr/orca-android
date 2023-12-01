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

package com.jeanbarrossilva.orca.feature.profiledetails.conversion

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter.DefaultProfileConverter
import com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter.EditableProfileConverter
import com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter.followable.FollowableProfileConverter
import kotlinx.coroutines.CoroutineScope

/** Creates an instance of a [ProfileConverter] through [create]. */
internal object ProfileConverterFactory {
  /**
   * Creates a [ProfileConverter].
   *
   * @param coroutineScope [CoroutineScope] through which converted [Profile]-related suspending
   *   will be performed.
   */
  fun create(coroutineScope: CoroutineScope): ProfileConverter {
    val defaultConverter = DefaultProfileConverter(next = null)
    val editableConverter = EditableProfileConverter(next = defaultConverter)
    return FollowableProfileConverter(coroutineScope, next = editableConverter)
  }
}
