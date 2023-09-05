package com.jeanbarrossilva.orca.core.mastodon.feed.profile.type.editable

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.MastodonProfile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.ProfileTootPaginateSource
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import java.net.URL

internal data class MastodonEditableProfile(
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
    Profile by MastodonProfile(
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
    override val editor = MastodonEditor()
}
