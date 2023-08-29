package com.jeanbarrossilva.orca.core.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.toot.mention.MentionableString
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

    /** What's actually been written. **/
    abstract val content: MentionableString

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
