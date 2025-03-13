/*
 * Copyright © 2024–2025 Orcinus
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

package br.com.orcinus.orca.core.sample.feed.profile.type.followable

import br.com.orcinus.orca.core.feed.profile.type.followable.Follow
import br.com.orcinus.orca.core.feed.profile.type.followable.FollowService
import br.com.orcinus.orca.core.feed.profile.type.followable.FollowableProfile
import br.com.orcinus.orca.core.sample.feed.profile.SampleProfileProvider

/** [FollowService] that toggles the [Follow] status of a [SampleFollowableProfile] in memory. */
class SampleFollowService(override val profileProvider: SampleProfileProvider) : FollowService() {
  override suspend fun <T : Follow> setFollow(profile: FollowableProfile<T>, follow: T) =
    profileProvider.update(profile.id) {
      @Suppress("UNCHECKED_CAST") (this as SampleFollowableProfile<Follow>).withFollow(follow)
    }

  override fun createNonFollowableProfileException(profileID: String) =
    NonFollowableProfileException(cause = null)
}
