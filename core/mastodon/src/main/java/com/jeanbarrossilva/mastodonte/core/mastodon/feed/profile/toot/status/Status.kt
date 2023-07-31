package com.jeanbarrossilva.mastodonte.core.mastodon.feed.profile.toot.status

import com.jeanbarrossilva.mastodonte.core.mastodon.feed.profile.account.MastodonAccount
import com.jeanbarrossilva.mastodonte.core.mastodon.feed.profile.toot.MastodonToot
import java.net.URL
import java.time.ZonedDateTime

data class Status internal constructor(
    internal val id: String,
    internal val createdAt: String,
    internal val account: MastodonAccount,
    internal val reblogsCount: Int,
    internal val favouritesCount: Int,
    internal val repliesCount: Int,
    internal val url: String,
    internal val text: String,
    @Suppress("SpellCheckingInspection") internal val favourited: Boolean?,
    internal val reblogged: Boolean?
) {
    internal fun toToot(): MastodonToot {
        val author = account.toAuthor()
        val publicationDateTime = ZonedDateTime.parse(createdAt)
        val url = URL(url)
        return MastodonToot(
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
