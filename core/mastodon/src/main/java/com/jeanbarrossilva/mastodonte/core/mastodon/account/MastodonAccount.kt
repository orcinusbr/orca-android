package com.jeanbarrossilva.mastodonte.core.mastodon.account

import com.jeanbarrossilva.mastodonte.core.account.at
import com.jeanbarrossilva.mastodonte.core.toot.Author
import java.net.URL

internal data class MastodonAccount(
    val id: String,
    val acct: String,
    val url: String,
    val displayName: String,
    val avatar: String
) {
    fun toAuthor(): Author {
        val avatarURL = URL(avatar)
        val account = with(acct.split('@')) {
            if (size == 2) first() at get(2) else first() at "mastodon.social"
        }
        val profileURL = URL(url)
        return Author(id, avatarURL, displayName, account, profileURL)
    }
}
