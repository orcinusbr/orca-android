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

package br.com.orcinus.orca.core.mastodon.feed.profile.type.editable

import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.feed.profile.type.editable.EditableProfile
import br.com.orcinus.orca.core.mastodon.feed.profile.MastodonProfile
import br.com.orcinus.orca.core.mastodon.feed.profile.MastodonProfilePostPaginator
import br.com.orcinus.orca.core.mastodon.feed.profile.post.MastodonPost
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.std.image.SomeImageLoader
import br.com.orcinus.orca.std.markdown.Markdown
import java.net.URI

/**
 * [MastodonProfile] that can be edited.
 *
 * @property requester [Requester] by which the [editor]'s editing requests are performed.
 * @property postPaginatorProvider [MastodonProfilePostPaginator.Provider] by which a
 *   [MastodonProfilePostPaginator] for paginating through the [MastodonProfile]'s [MastodonPost]s
 *   will be provided.
 */
internal class MastodonEditableProfile(
  private val requester: Requester,
  private val postPaginatorProvider: MastodonProfilePostPaginator.Provider,
  override val id: String,
  override val account: Account,
  override val avatarLoader: SomeImageLoader,
  override val name: String,
  override val bio: Markdown,
  override val followerCount: Int,
  override val followingCount: Int,
  override val uri: URI
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
    uri
  ),
  EditableProfile() {
  override val editor = MastodonEditor(requester)
}
