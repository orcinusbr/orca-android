/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.feature.profiledetails.conversion

import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.type.followable.Follow
import br.com.orcinus.orca.core.feed.profile.type.followable.FollowService
import br.com.orcinus.orca.feature.profiledetails.conversion.converter.DefaultProfileConverter
import br.com.orcinus.orca.feature.profiledetails.conversion.converter.EditableProfileConverter
import br.com.orcinus.orca.feature.profiledetails.conversion.converter.followable.FollowableProfileConverter
import kotlinx.coroutines.CoroutineScope

/** Creates an instance of a [ProfileConverter] through [create]. */
internal object ProfileConverterFactory {
  /**
   * Creates a [ProfileConverter].
   *
   * @param coroutineScope [CoroutineScope] through which converted [Profile]-related suspending
   *   will be performed.
   * @param followService [FollowService] by which the [Follow] status can be toggled.
   * @see Follow.toggled
   */
  fun create(coroutineScope: CoroutineScope, followService: FollowService): ProfileConverter {
    val defaultConverter = DefaultProfileConverter(next = null)
    val editableConverter = EditableProfileConverter(next = defaultConverter)
    return FollowableProfileConverter(coroutineScope, followService, next = editableConverter)
  }
}
