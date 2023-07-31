package com.jeanbarrossilva.mastodonte.core.mastodon.feed.profile.type.followable

import com.jeanbarrossilva.mastodonte.core.account.Account
import com.jeanbarrossilva.mastodonte.core.feed.profile.Profile
import com.jeanbarrossilva.mastodonte.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.mastodonte.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.mastodonte.core.mastodon.client.MastodonHttpClient
import com.jeanbarrossilva.mastodonte.core.mastodon.client.authenticateAndPost
import com.jeanbarrossilva.mastodonte.core.mastodon.feed.profile.MastodonProfile
import com.jeanbarrossilva.mastodonte.core.mastodon.feed.profile.toot.status.TootPaginateSource
import java.net.URL

internal data class MastodonFollowableProfile<T : Follow>(
    private val tootPaginateSource: TootPaginateSource,
    override val id: String,
    override val account: Account,
    override val avatarURL: URL,
    override val name: String,
    override val bio: String,
    override val follow: Follow,
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
    FollowableProfile<Follow>() {
    override suspend fun onChangeFollowTo(follow: Follow) {
        val toggledRoute = follow.getToggledRoute(this)
        MastodonHttpClient.authenticateAndPost(toggledRoute)
    }
}
