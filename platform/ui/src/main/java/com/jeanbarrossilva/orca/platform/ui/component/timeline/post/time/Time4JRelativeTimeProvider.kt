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

import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Locale
import net.time4j.PrettyTime
import net.time4j.base.UnixTime

/** [RelativeTimeProvider] that provides relative time with Time4J. */
internal class Time4JRelativeTimeProvider : RelativeTimeProvider() {
  override fun onProvide(dateTime: ZonedDateTime): String {
    val locale = Locale.getDefault()
    val unixTime = dateTime.toUnixTime()
    val timeZoneID = ZoneId.systemDefault().id
    return PrettyTime.of(locale).printRelative(unixTime, timeZoneID)
  }

  /** Converts this [ZonedDateTime] into a [UnixTime]. */
  private fun ZonedDateTime.toUnixTime(): UnixTime {
    return object : UnixTime {
      override fun getPosixTime(): Long {
        return toEpochSecond()
      }

      override fun getNanosecond(): Int {
        return nano
      }
    }
  }
}
