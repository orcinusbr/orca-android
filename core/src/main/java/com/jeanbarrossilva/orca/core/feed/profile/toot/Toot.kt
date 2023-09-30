package com.jeanbarrossilva.orca.core.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import java.io.Serializable
import java.net.URL
import java.time.ZonedDateTime
import kotlinx.coroutines.flow.Flow

/** Content that's been posted by a user, the [author]. **/
abstract class Toot : Serializable {
    /** Unique identifier. **/
    abstract val id: String

    /** [Author] that has authored this [Toot]. **/
    abstract val author: Author

    /** [Content] that's been composed by the [author]. **/
    abstract val content: Content

    /** Zoned moment in time in which this [Toot] was published. **/
    abstract val publicationDateTime: ZonedDateTime

    /** Number of comments made in response to this [Toot]. **/
    abstract val commentCount: Int

    /** Whether this [Toot] is marked as favorite. **/
    abstract val isFavorite: Boolean

    /** Number of favorites given by users to this [Toot]. **/
    abstract val favoriteCount: Int

    /** Whether this [Toot] is reblogged. **/
    abstract val isReblogged: Boolean

    /** Number of times this [Toot] has been re-blogged. **/
    abstract val reblogCount: Int

    /** [URL] that leads to this [Toot]. **/
    abstract val url: URL

    /**
     * Toggles the "favorite" state of this [Toot].
     *
     * @see isFavorite
     **/
    suspend fun toggleFavorite() {
        setFavorite(!isFavorite)
    }

    /**
     * Toggles the "reblogged" state of this [Toot].
     *
     * @see isReblogged
     **/
    suspend fun toggleReblogged() {
        setReblogged(!isReblogged)
    }

    /**
     * Copies this [Toot].
     *
     * @param id Unique identifier.
     * @param author [Author] that has authored this [Toot].
     * @param content [Content] that's been composed by the [author].
     * @param publicationDateTime Zoned moment in time in which this [Toot] was published.
     * @param commentCount Number of comments made in response to this [Toot].
     * @param isFavorite Whether this [Toot] is marked as favorite.
     * @param favoriteCount Number of favorites given by users to this [Toot].
     * @param isReblogged Whether this [Toot] is reblogged.
     * @param reblogCount Number of times this [Toot] has been reblogged.
     * @param url [URL] that leads to this [Toot].
     **/
    fun copy(
        id: String = this.id,
        author: Author = this.author,
        content: Content = this.content,
        publicationDateTime: ZonedDateTime = this.publicationDateTime,
        commentCount: Int = this.commentCount,
        isFavorite: Boolean = this.isFavorite,
        favoriteCount: Int = this.favoriteCount,
        isReblogged: Boolean = this.isReblogged,
        reblogCount: Int = this.reblogCount,
        url: URL = this.url
    ): Toot {
        return object : Toot() {
            override val id = id
            override val author = author
            override val content = content
            override val publicationDateTime = publicationDateTime
            override val commentCount = commentCount
            override val isFavorite = isFavorite
            override val favoriteCount = favoriteCount
            override val isReblogged = isReblogged
            override val reblogCount = reblogCount
            override val url = url

            override suspend fun getComments(page: Int): Flow<List<Toot>> {
                return this@Toot.getComments(page)
            }

            override suspend fun setFavorite(isFavorite: Boolean) {
                this@Toot.setFavorite(isFavorite)
            }

            override suspend fun setReblogged(isReblogged: Boolean) {
                this@Toot.setReblogged(isReblogged)
            }
        }
    }

    abstract suspend fun getComments(page: Int): Flow<List<Toot>>

    /**
     * Defines whether this [Toot] is marked as favorite.
     *
     * @param isFavorite Whether it's marked as favorite.
     **/
    protected abstract suspend fun setFavorite(isFavorite: Boolean)

    /**
     * Defines whether this [Toot] is reblogged.
     *
     * @param isReblogged Whether it's reblogged.
     **/
    protected abstract suspend fun setReblogged(isReblogged: Boolean)

    companion object
}
