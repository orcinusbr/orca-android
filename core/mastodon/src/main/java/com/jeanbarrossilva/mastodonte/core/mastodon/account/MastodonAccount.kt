package com.jeanbarrossilva.mastodonte.core.mastodon.account

import com.jeanbarrossilva.mastodonte.core.account.Account
import com.jeanbarrossilva.mastodonte.core.feed.profile.Profile
import com.jeanbarrossilva.mastodonte.core.feed.profile.toot.Author
import com.jeanbarrossilva.mastodonte.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.mastodonte.core.mastodon.client.MastodonHttpClient
import com.jeanbarrossilva.mastodonte.core.mastodon.client.authenticateAndGet
import com.jeanbarrossilva.mastodonte.core.mastodon.feed.profile.toot.status.TootPaginateSource
import com.jeanbarrossilva.mastodonte.core.mastodon.feed.profile.type.editable.MastodonEditableProfile
import com.jeanbarrossilva.mastodonte.core.mastodon.feed.profile.type.followable.MastodonFollowableProfile
import io.ktor.client.call.body
import io.ktor.http.parametersOf
import java.net.URL
import kotlinx.serialization.Serializable

@Serializable
internal data class MastodonAccount(
    val id: String,
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

    suspend fun toProfile(tootPaginateSource: TootPaginateSource): Profile {
        return if (isOwner()) {
            toEditableProfile(tootPaginateSource)
        } else {
            toFollowableProfile(tootPaginateSource)
        }
    }

    private fun toAccount(): Account {
        return Account.of(acct, "mastodon.social")
    }

    private suspend fun isOwner(): Boolean {
        val credentialAccount = MastodonHttpClient
            .authenticateAndGet("/api/v1/accounts/verify_credentials")
            .body<CredentialAccount>()
        return id == credentialAccount.id
    }

    private fun toEditableProfile(tootPaginateSource: TootPaginateSource): MastodonEditableProfile {
        val account = toAccount()
        val avatarURL = URL(avatar)
        val url = URL(url)
        return MastodonEditableProfile(
            tootPaginateSource,
            id,
            account,
            avatarURL,
            displayName,
            bio = note,
            followersCount,
            followingCount,
            url
        )
    }

    private suspend fun toFollowableProfile(tootPaginateSource: TootPaginateSource):
            MastodonFollowableProfile<Follow> {
        val account = toAccount()
        val avatarURL = URL(avatar)
        val url = URL(url)
        val follow = MastodonHttpClient
            .authenticateAndGet("/api/v1/accounts/relationships") { parametersOf("id", listOf(id)) }
            .body<List<Relationship>>()
            .first()
            .toFollow(this)
        return MastodonFollowableProfile(
            tootPaginateSource,
            id,
            account,
            avatarURL,
            displayName,
            bio = note,
            follow,
            followersCount,
            followingCount,
            url
        )
    }
}
