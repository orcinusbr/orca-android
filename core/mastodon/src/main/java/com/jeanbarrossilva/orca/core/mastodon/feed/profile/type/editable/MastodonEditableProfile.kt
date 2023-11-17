package com.jeanbarrossilva.orca.core.mastodon.feed.profile.type.editable

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.MastodonProfile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.MastodonProfileTootPaginator
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.MastodonToot
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import java.net.URL

/**
 * [MastodonProfile] that can be edited.
 *
 * @param tootPaginatorProvider [MastodonProfileTootPaginator.Provider] by which a
 *   [MastodonProfileTootPaginator] for paginating through the [MastodonProfile]'s [MastodonToot]s
 *   will be provided.
 */
internal data class MastodonEditableProfile(
  private val tootPaginatorProvider: MastodonProfileTootPaginator.Provider,
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
    tootPaginatorProvider,
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
