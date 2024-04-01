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

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.type.editable

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.MastodonProfile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.MastodonProfilePostPaginator
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.MastodonPost
import com.jeanbarrossilva.orca.std.image.SomeImageLoader
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import java.net.URL

/**
 * [MastodonProfile] that can be edited.
 *
 * @param postPaginatorProvider [MastodonProfilePostPaginator.Provider] by which a
 *   [MastodonProfilePostPaginator] for paginating through the [MastodonProfile]'s [MastodonPost]s
 *   will be provided.
 */
internal data class MastodonEditableProfile(
  private val postPaginatorProvider: MastodonProfilePostPaginator.Provider,
  override val id: String,
  override val account: Account,
  override val avatarLoader: SomeImageLoader,
  override val name: String,
  override val bio: StyledString,
  override val followerCount: Int,
  override val followingCount: Int,
  override val url: URL
) :
  Profile by MastodonProfile(
    postPaginatorProvider,
    id,
    account,
    avatarLoader,
    name,
    bio,
    followerCount,
    followingCount,
    url
  ),
  EditableProfile() {
  override val editor = MastodonEditor()
}
