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

package br.com.orcinus.orca.feature.profiledetails.conversion.converter.followable

import br.com.orcinus.orca.autos.colors.Colors
import br.com.orcinus.orca.composite.timeline.text.annotated.toAnnotatedString
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.type.followable.Follow
import br.com.orcinus.orca.core.feed.profile.type.followable.FollowService
import br.com.orcinus.orca.core.feed.profile.type.followable.FollowableProfile
import br.com.orcinus.orca.feature.profiledetails.ProfileDetails
import br.com.orcinus.orca.feature.profiledetails.conversion.ProfileConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * [ProfileConverter] that converts a [FollowableProfile].
 *
 * @param coroutineScope [CoroutineScope] through which converted [Profile]'s [Follow] status toggle
 *   will be performed.
 * @param followService [FollowService] by which the [Follow] status can be toggled.
 * @see Follow.toggled
 */
internal class FollowableProfileConverter(
  private val coroutineScope: CoroutineScope,
  private val followService: FollowService,
  override val next: ProfileConverter?
) : ProfileConverter() {
  override fun onConvert(profile: Profile, colors: Colors): ProfileDetails? {
    return if (profile is FollowableProfile<*>) {
      ProfileDetails.Followable(
        profile.id,
        profile.avatarLoader,
        profile.name,
        profile.account,
        profile.bio.toAnnotatedString(colors),
        profile.uri,
        profile.follow.toStatus()
      ) {
        coroutineScope.launch { followService.toggle(profile.id, profile.follow) }
      }
    } else {
      null
    }
  }
}
