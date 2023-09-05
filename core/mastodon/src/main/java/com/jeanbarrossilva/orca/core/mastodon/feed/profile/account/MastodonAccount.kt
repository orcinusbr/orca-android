package com.jeanbarrossilva.orca.core.mastodon.feed.profile.account

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.core.mastodon.client.MastodonHttpClient
import com.jeanbarrossilva.orca.core.mastodon.client.authenticateAndGet
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.ProfileTootPaginateSource
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.type.editable.MastodonEditableProfile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.type.followable.MastodonFollowableProfile
import com.jeanbarrossilva.orca.platform.ui.core.style.fromHtml
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import java.net.URL
import kotlinx.serialization.Serializable

@Serializable
internal data class MastodonAccount(
    val id: String,
    val username: String,
    val acct: String,
    val url: String,
    val displayName: String,
    val locked: Boolean,
    val note: String,
    val avatar: String,
    val followersCount: Int,
    val followingCount: Int
) {
    fun toAuthor(): Author {
        val avatarURL = URL(avatar)
        val account = toAccount()
        val profileURL = URL(url)
        return Author(id, avatarURL, displayName, account, profileURL)
    }

    fun toAccount(): Account {
        return Account.of(acct, fallbackInstance = "mastodon.social")
    }

    suspend fun toProfile(tootPaginateSourceProvider: ProfileTootPaginateSource.Provider): Profile {
        return if (isOwner()) {
            toEditableProfile(tootPaginateSourceProvider)
        } else {
            toFollowableProfile(tootPaginateSourceProvider)
        }
    }

    private suspend fun isOwner(): Boolean {
        val credentialAccount = MastodonHttpClient
            .authenticateAndGet("/api/v1/accounts/verify_credentials")
            .body<CredentialAccount>()
        return id == credentialAccount.id
    }

    private fun toEditableProfile(tootPaginateSourceProvider: ProfileTootPaginateSource.Provider):
        MastodonEditableProfile {
        val account = toAccount()
        val avatarURL = URL(avatar)
        val bio = StyledString.fromHtml(note)
        val url = URL(url)
        return MastodonEditableProfile(
            tootPaginateSourceProvider,
            id,
            account,
            avatarURL,
            displayName,
            bio,
            followersCount,
            followingCount,
            url
        )
    }

    private suspend fun toFollowableProfile(
        tootPaginateSourceProvider: ProfileTootPaginateSource.Provider
    ): MastodonFollowableProfile<Follow> {
        val account = toAccount()
        val avatarURL = URL(avatar)
        val bio = StyledString.fromHtml(note)
        val url = URL(url)
        val follow = MastodonHttpClient
            .authenticateAndGet("/api/v1/accounts/relationships") { parameter("id", id) }
            .body<List<Relationship>>()
            .first()
            .toFollow(this)
        return MastodonFollowableProfile(
            tootPaginateSourceProvider,
            id,
            account,
            avatarURL,
            displayName,
            bio,
            follow,
            followersCount,
            followingCount,
            url
        )
    }
}
