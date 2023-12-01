/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
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
