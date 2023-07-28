package com.jeanbarrossilva.mastodonte.core.mastodon.account

import com.jeanbarrossilva.mastodonte.core.account.Account
import com.jeanbarrossilva.mastodonte.core.auth.AuthenticationLock
import com.jeanbarrossilva.mastodonte.core.mastodon.Mastodon
import com.jeanbarrossilva.mastodonte.core.mastodon.profile.edit.MastodonEditableProfile
import com.jeanbarrossilva.mastodonte.core.mastodon.profile.follow.MastodonFollowableProfile
import com.jeanbarrossilva.mastodonte.core.mastodon.toot.status.TootPaginateSource
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.follow.Follow
import com.jeanbarrossilva.mastodonte.core.toot.Author
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
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

    suspend fun toProfile(
        authenticationLock: AuthenticationLock,
        tootPaginateSource: TootPaginateSource
    ): Profile {
        val isOwner = isOwner(authenticationLock)
        return if (isOwner) {
            toEditableProfile(authenticationLock, tootPaginateSource)
        } else {
            toFollowableProfile(authenticationLock, tootPaginateSource)
        }
    }

    private fun toAccount(): Account {
        return Account.of(acct, "mastodon.social")
    }

    private suspend fun isOwner(authenticationLock: AuthenticationLock): Boolean {
        val credentialAccount = Mastodon
            .httpClient
            .get("/api/v1/accounts/verify_credentials") {
                authenticationLock.unlock {
                    bearerAuth(it.accessToken)
                }
            }
            .body<CredentialAccount>()
        return id == credentialAccount.id
    }

    private fun toEditableProfile(
        authenticationLock: AuthenticationLock,
        tootPaginateSource: TootPaginateSource
    ): MastodonEditableProfile {
        val account = toAccount()
        val avatarURL = URL(avatar)
        val url = URL(url)
        return MastodonEditableProfile(
            authenticationLock,
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

    private suspend fun toFollowableProfile(
        authenticationLock: AuthenticationLock,
        tootPaginateSource: TootPaginateSource
    ): MastodonFollowableProfile<Follow> {
        val account = toAccount()
        val avatarURL = URL(avatar)
        val url = URL(url)
        val follow = getFollow(authenticationLock)
        return MastodonFollowableProfile(
            authenticationLock,
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

    private suspend fun getFollow(authenticationLock: AuthenticationLock): Follow {
        return Mastodon
            .httpClient
            .get("/api/v1/accounts/relationships") {
                authenticationLock.unlock { bearerAuth(it.accessToken) }
                parametersOf("id", listOf(id))
            }
            .body<List<Relationship>>()
            .first()
            .toFollow(this)
    }
}
