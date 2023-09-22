package com.jeanbarrossilva.orca.core.http.feed.profile.toot.status

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.core.http.feed.profile.account.HttpAccount
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.HttpToot
import com.jeanbarrossilva.orca.platform.ui.core.style.fromHtml
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import java.net.URL
import java.time.ZonedDateTime
import kotlinx.serialization.Serializable

/**
 * Structure returned by the API that is the DTO version of an [HttpToot].
 *
 * @param id Unique identifier.
 * @param createdAt ISO-8601-formatted [String] indicating the date and time of creation.
 * @param account [HttpAccount] of the author that's created this [HttpStatus].
 * @param reblogsCount Amount of times it's been reblogged.
 * @param favouritesCount Amount of times it's been favorited.
 * @param repliesCount Amount of replies that's been received.
 * @param url String [URL] that leads to this [HttpStatus].
 * @param card [HttpCard] for the highlighted [URL] that's in the [content].
 * @param content Text that's been written.
 * @param mediaAttachments [HttpAttachment]s of attached media.
 * @param favourited Whether it's been favorited by the currently
 * [authenticated][Actor.Authenticated] [Actor].
 * @param reblogged Whether the [authenticated][Actor.Authenticated] [Actor] has reblogged this
 * [HttpStatus].
 **/
@Serializable
data class HttpStatus internal constructor(
    internal val id: String,
    internal val createdAt: String,
    internal val account: HttpAccount,
    internal val reblogsCount: Int,
    internal val favouritesCount: Int,
    internal val repliesCount: Int,
    internal val url: String,
    internal val card: HttpCard?,
    internal val content: String,
    internal val mediaAttachments: List<HttpAttachment>,
    internal val favourited: Boolean?,
    internal val reblogged: Boolean?
) {
    /** Converts this [HttpStatus] into an [HttpToot]. **/
    internal fun toToot(): HttpToot {
        val author = this.account.toAuthor()
        val text = StyledString.fromHtml(content)
        val attachments = mediaAttachments.map(HttpAttachment::toAttachment)
        val content = Content.from(text, attachments) { card?.toHeadline() }
        val publicationDateTime = ZonedDateTime.parse(createdAt)
        val url = URL(url)
        return HttpToot(
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
