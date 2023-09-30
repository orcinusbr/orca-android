package com.jeanbarrossilva.orca.core.feed.profile.toot.reblog

import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import java.net.URL
import java.time.ZonedDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * [Toot] that has been reblogged by someone else.
 *
 * @param original [Toot] from which the [Reblog] derives.
 * @param reblogger [Author] by which the [Toot] has been reblogged.
 **/
fun Reblog(original: Toot, reblogger: Author): Reblog {
    return Reblog(
        original.id,
        original.author,
        reblogger,
        original.content,
        original.publicationDateTime,
        original.commentCount,
        original.isFavorite,
        original.favoriteCount,
        original.isReblogged,
        original.reblogCount,
        original.url,
        original::getComments,
        { original.toggleFavorite() }
    ) {
        original.toggleReblogged()
    }
}

/**
 * [Toot] that has been reblogged by someone else.
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
 * @param getComments Gets the comments made in response to the [Toot].
 * @param setFavorite Defines whether the [Toot] is marked as favorite.
 * @param setReblogged Defines whether the [Toot] is reblogged.
 **/
fun Reblog(
    id: String,
    author: Author,
    reblogger: Author,
    content: Content,
    publicationDateTime: ZonedDateTime,
    commentCount: Int,
    isFavorite: Boolean,
    favoriteCount: Int,
    isReblogged: Boolean,
    reblogCount: Int,
    url: URL,
    getComments: suspend (page: Int) -> Flow<List<Toot>> = { emptyFlow() },
    setFavorite: suspend (isFavorite: Boolean) -> Unit = { },
    setReblogged: suspend (isReblogged: Boolean) -> Unit = { }
): Reblog {
    return object : Reblog() {
        override val id = id
        override val author = author
        override val reblogger = reblogger
        override val content = content
        override val publicationDateTime = publicationDateTime
        override val commentCount = commentCount
        override val isFavorite = isFavorite
        override val favoriteCount = favoriteCount
        override val isReblogged = isReblogged
        override val reblogCount = reblogCount
        override val url = url

        override suspend fun getComments(page: Int): Flow<List<Toot>> {
            return getComments(page)
        }

        override suspend fun setFavorite(isFavorite: Boolean) {
            setFavorite(isFavorite)
        }

        override suspend fun setReblogged(isReblogged: Boolean) {
            setReblogged(isReblogged)
        }
    }
}
