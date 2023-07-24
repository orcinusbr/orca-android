package com.jeanbarrossilva.mastodonte.core.mastodon.toot

import com.jeanbarrossilva.mastodonte.core.auth.AuthenticationLock
import com.jeanbarrossilva.mastodonte.core.mastodon.account.MastodonAccount
import java.net.URL
import java.time.ZonedDateTime

internal data class Status(
    val id: String,
    val createdAt: String,
    val account: MastodonAccount,
    val reblogsCount: Int,
    val favouritesCount: Int,
    val repliesCount: Int,
    val url: String,
    val text: String,
    @Suppress("SpellCheckingInspection") val favourited: Boolean?,
    val reblogged: Boolean?
) {
    fun toToot(authenticationLock: AuthenticationLock): MastodonToot {
        val author = account.toAuthor()
        val publicationDateTime = ZonedDateTime.parse(createdAt)
        val url = URL(url)
        return MastodonToot(
            authenticationLock,
            id,
            author,
            text,
            publicationDateTime,
            repliesCount,
            favourited == true,
            favouritesCount,
            reblogged == true,
            reblogsCount,
            url
        )
    }
}
