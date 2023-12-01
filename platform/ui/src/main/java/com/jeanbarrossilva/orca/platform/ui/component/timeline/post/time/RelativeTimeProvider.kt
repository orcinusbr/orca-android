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

package com.jeanbarrossilva.orca.platform.ui.component.timeline.post.time

import java.time.ZonedDateTime

/** Provides a relative version of a [ZonedDateTime]. */
abstract class RelativeTimeProvider {
  /**
   * Provides the relative version of [dateTime].
   *
   * @param dateTime [ZonedDateTime] whose relative version will be provided.
   */
  internal fun provide(dateTime: ZonedDateTime): String {
    return onProvide(dateTime)
  }

  /**
   * Provides the relative version of [dateTime].
   *
   * @param dateTime [ZonedDateTime] whose relative version will be provided.
   */
  protected abstract fun onProvide(dateTime: ZonedDateTime): String
}
