package com.jeanbarrossilva.mastodonte.core.mastodon.account

import com.jeanbarrossilva.mastodonte.core.account.Account
import com.jeanbarrossilva.mastodonte.core.toot.Author
import kotlinx.serialization.Serializable
import java.net.URL

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

    private fun toAccount(): Account {
        return Account.of(acct, "mastodon.social")
    }
}
