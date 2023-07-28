package com.jeanbarrossilva.mastodonte.core.mastodon.profile.follow

import com.jeanbarrossilva.mastodonte.core.account.Account
import com.jeanbarrossilva.mastodonte.core.auth.AuthenticationLock
import com.jeanbarrossilva.mastodonte.core.mastodon.Mastodon
import com.jeanbarrossilva.mastodonte.core.mastodon.profile.MastodonProfile
import com.jeanbarrossilva.mastodonte.core.mastodon.toot.status.TootPaginateSource
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.follow.Follow
import com.jeanbarrossilva.mastodonte.core.profile.follow.FollowableProfile
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import java.net.URL

internal data class MastodonFollowableProfile<T : Follow>(
    private val authenticationLock: AuthenticationLock,
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
        Mastodon.httpClient.post(toggledRoute) {
            authenticationLock.unlock {
                bearerAuth(it.accessToken)
            }
        }
    }
}
