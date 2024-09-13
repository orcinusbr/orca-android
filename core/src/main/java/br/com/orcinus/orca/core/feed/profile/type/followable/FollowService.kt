/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.core.feed.profile.type.followable

import br.com.orcinus.orca.core.InternalCoreApi
import br.com.orcinus.orca.core.auth.actor.Actor

/**
 * Service by which the [Follow] status of a [FollowableProfile] in relation to another is changed.
 */
abstract class FollowService @InternalCoreApi constructor() {
  /**
   * Toggles the [Follow] status of the currently authenticated [Actor] regarding the specified
   * [FollowableProfile].
   *
   * @param profileID ID of the [FollowableProfile] to follow, request to follow or unfollow.
   * @see FollowableProfile.follow
   * @see FollowableProfile.id
   */
  abstract suspend fun toggle(profileID: String, follow: Follow)
}
