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

package br.com.orcinus.orca.feature.profiledetails

import br.com.orcinus.orca.autos.colors.Colors
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.type.followable.Follow
import br.com.orcinus.orca.core.feed.profile.type.followable.FollowService
import br.com.orcinus.orca.feature.profiledetails.conversion.ProfileConverterFactory
import kotlinx.coroutines.CoroutineScope

/**
 * Converts this core [ProfileDetails] into [ProfileDetails].
 *
 * @param coroutineScope [CoroutineScope] through which converted [Profile]-related suspending will
 *   be performed.
 * @param followService [FollowService] by which the [Follow] status can be toggled.
 * @param colors [Colors] by which visuals can be colored.
 * @see Follow.toggled
 */
internal fun Profile.toProfileDetails(
  coroutineScope: CoroutineScope,
  followService: FollowService,
  colors: Colors
): ProfileDetails {
  val details = ProfileConverterFactory.create(coroutineScope, followService).convert(this, colors)
  return requireNotNull(details)
}
