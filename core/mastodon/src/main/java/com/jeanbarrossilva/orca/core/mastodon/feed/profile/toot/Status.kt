package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot

import android.text.Html
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.toStyledString
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.account.MastodonAccount
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style.type.MastodonHashtagDelimiter
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style.type.MastodonLinkDelimiter
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style.type.MastodonMentionDelimiter
import java.net.URL
import java.time.ZonedDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Status internal constructor(
    internal val id: String,
    internal val createdAt: String,
    internal val account: MastodonAccount,
    internal val reblogsCount: Int,
    internal val favouritesCount: Int,
    internal val repliesCount: Int,
    internal val url: String,
    internal val content: String,
    @Suppress("SpellCheckingInspection") internal val favourited: Boolean?,
    internal val reblogged: Boolean?
) {

    internal fun toToot(): MastodonToot {
        val author = this.account.toAuthor()
        val mentionDelimiter = MastodonMentionDelimiter(this)
        val encodedContent = content.replace("<p>", "").replace("</p>", "").toStyledString(
            hashtagDelimiter = MastodonHashtagDelimiter(this),
            mentionDelimiter = mentionDelimiter,
            linkDelimiter = MastodonLinkDelimiter
        ) {
            mentionDelimiter.getNextURL()
        }
        val content = Html
            .fromHtml("$encodedContent", Html.FROM_HTML_MODE_COMPACT)
            .toString()
            .toStyledString()
        val publicationDateTime = ZonedDateTime.parse(createdAt)
        val url = URL(url)
        return MastodonToot(
            id,
            author,
            content,
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
