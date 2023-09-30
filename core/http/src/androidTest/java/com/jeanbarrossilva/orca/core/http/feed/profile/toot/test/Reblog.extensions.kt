package com.jeanbarrossilva.orca.core.http.feed.profile.toot.test

import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.toot.reblog.Reblog
import java.net.URL
import java.time.ZonedDateTime

/**
 * Copies this [Reblog].
 *
 * @param id Unique identifier.
 * @param author [Author] that has authored the [Toot].
 * @param reblogger [Author] by which the [Toot] has been reblogged.
 * @param content [Content] that's been composed by the [author].
 * @param publicationDateTime Zoned moment in time in which the [Toot] was published.
 * @param commentCount Number of comments made in response to the [Toot].
 * @param isFavorite Whether the [Toot] is marked as favorite.
 * @param favoriteCount Number of favorites given by users to the [Toot].
 * @param isReblogged Whether the [Toot] is reblogged.
 * @param reblogCount Number of times the [Toot] has been reblogged.
 * @param url [URL] that leads to the [Toot].
 **/
internal fun Reblog.copy(
    id: String = this.id,
    author: Author = this.author,
    reblogger: Author = this.reblogger,
    content: Content = this.content,
    publicationDateTime: ZonedDateTime = this.publicationDateTime,
    commentCount: Int = this.commentCount,
    isFavorite: Boolean = this.isFavorite,
    favoriteCount: Int = this.favoriteCount,
    isReblogged: Boolean = this.isReblogged,
    reblogCount: Int = this.reblogCount,
    url: URL = this.url
): Reblog {
    return Reblog(
        id,
        author,
        reblogger,
        content,
        publicationDateTime,
        commentCount,
        isFavorite,
        favoriteCount,
        isReblogged,
        reblogCount,
        url
    )
}
