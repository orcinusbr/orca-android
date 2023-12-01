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

package com.jeanbarrossilva.orca.feature.postdetails.ui.header

import java.text.DateFormat
import java.time.Instant
import java.time.ZonedDateTime
import java.util.Date

internal val ZonedDateTime.formatted: String
  get() = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(toDate())

/** Converts this [ZonedDateTime] into a [Date]. */
private fun ZonedDateTime.toDate(): Date {
  val epochSecond = toEpochSecond()
  val instant = Instant.ofEpochSecond(epochSecond)
  return Date.from(instant)
}
