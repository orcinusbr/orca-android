package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.status

import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.account.MastodonAccount
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.MastodonToot
import com.jeanbarrossilva.orca.platform.ui.core.style.fromHtml
import com.jeanbarrossilva.orca.std.styledstring.StyledString
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
    internal val card: Card?,
    internal val content: String,
    internal val mediaAttachments: List<MastodonAttachment>,
    @Suppress("SpellCheckingInspection") internal val favourited: Boolean?,
    internal val reblogged: Boolean?
) {

    internal fun toToot(): MastodonToot {
        val author = this.account.toAuthor()
        val text = StyledString.fromHtml(content)
        val attachments = mediaAttachments.map(MastodonAttachment::toAttachment)
        val content = Content.from(text, attachments) { card?.toHeadline() }
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
