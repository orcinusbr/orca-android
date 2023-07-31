package com.jeanbarrossilva.mastodonte.core.mastodon.feed.profile.type.editable

import com.jeanbarrossilva.mastodonte.core.feed.profile.Profile
import com.jeanbarrossilva.mastodonte.core.feed.profile.account.Account
import com.jeanbarrossilva.mastodonte.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.mastodonte.core.mastodon.feed.profile.MastodonProfile
import com.jeanbarrossilva.mastodonte.core.mastodon.feed.profile.toot.status.TootPaginateSource
import java.net.URL

internal data class MastodonEditableProfile(
    private val tootPaginateSource: TootPaginateSource,
    override val id: String,
    override val account: Account,
    override val avatarURL: URL,
    override val name: String,
    override val bio: String,
    override val followerCount: Int,
    override val followingCount: Int,
    override val url: URL
) :
    Profile by MastodonProfile(
        tootPaginateSource,
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
