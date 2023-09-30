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
     * Converts this [Toot] into a [Reblog], reblogged by the [reblogger].
     *
     * @param reblogger [Author] by which this [Toot] has been reblogged.
     **/
    fun toReblog(reblogger: Author): Reblog {
        return object : Reblog() {
            override val id = this@Toot.id
            override val author = this@Toot.author
            override val reblogger = reblogger
            override val content = this@Toot.content
            override val publicationDateTime = this@Toot.publicationDateTime
            override val commentCount = this@Toot.commentCount
            override val isFavorite = this@Toot.isFavorite
            override val favoriteCount = this@Toot.favoriteCount
            override val isReblogged = this@Toot.isReblogged
            override val reblogCount = this@Toot.reblogCount
            override val url = this@Toot.url

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
