package com.jeanbarrossilva.orca.core.http.feed.profile.type.editable

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.http.feed.profile.HttpProfile
import com.jeanbarrossilva.orca.core.http.feed.profile.ProfileTootPaginateSource
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.HttpToot
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import java.net.URL

/**
 * [HttpProfile] that can be edited.
 *
 * @param tootPaginateSourceProvider [ProfileTootPaginateSource.Provider] by which a
 *   [ProfileTootPaginateSource] for paginating through the [HttpProfile]'s [HttpToot]s will be
 *   provided.
 */
internal data class HttpEditableProfile(
  private val tootPaginateSourceProvider: ProfileTootPaginateSource.Provider,
  override val id: String,
  override val account: Account,
  override val avatarURL: URL,
  override val name: String,
  override val bio: StyledString,
  override val followerCount: Int,
  override val followingCount: Int,
  override val url: URL
) :
  Profile by HttpProfile(
    tootPaginateSourceProvider,
    id,
    account,
    avatarURL,
    name,
    bio,
    followerCount,
    followingCount,
    url
  ),
  EditableProfile() {
  override val editor = HttpEditor()
}
