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

package br.com.orcinus.orca.core.sample.feed.profile.type.followable

import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.type.followable.Follow
import br.com.orcinus.orca.core.feed.profile.type.followable.FollowableProfile
import br.com.orcinus.orca.core.sample.feed.profile.SampleProfile
import br.com.orcinus.orca.core.sample.feed.profile.SampleProfileProvider
import br.com.orcinus.orca.core.sample.feed.profile.post.SamplePostProvider
import br.com.orcinus.orca.std.markdown.Markdown

/**
 * [SampleProfile] that's also followable.
 *
 * @param provider [SampleProfileProvider] through which the [Follow] status can be updated.
 * @see FollowableProfile
 */
internal data class SampleFollowableProfile<T : Follow>(
  private val provider: SampleProfileProvider,
  private val postProvider: SamplePostProvider,
  private val delegate: Author,
  override val bio: Markdown,
  override val follow: T,
  override val followerCount: Int,
  override val followingCount: Int
) :
  Profile by SampleProfile(postProvider, delegate, bio, followerCount, followingCount),
  FollowableProfile<T>() {
  override suspend fun onChangeFollowTo(follow: T) {
    provider.updateFollow(id, follow)
  }
}
