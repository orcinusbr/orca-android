package com.jeanbarrossilva.orca.core.sample.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import java.net.URL
import java.time.ZonedDateTime
import java.util.Objects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/** [Toot] whose operations are performed in memory and serves as a sample. **/
internal class SampleToot(
    override val id: String,
    override val author: Author,
    override val content: Content,
    override val publicationDateTime: ZonedDateTime,
    override val commentCount: Int,
    override val isFavorite: Boolean,
    override val favoriteCount: Int,
    override val isReblogged: Boolean,
    override val reblogCount: Int,
    override val url: URL
) : Toot() {
    override fun equals(other: Any?): Boolean {
        return other is SampleToot &&
            id == other.id &&
            author == other.author &&
            content == other.content &&
            publicationDateTime == other.publicationDateTime &&
            commentCount == other.commentCount &&
            isFavorite == other.isFavorite &&
            favoriteCount == other.favoriteCount &&
            isReblogged == other.isReblogged &&
            reblogCount == other.reblogCount &&
            url == other.url
    }

    override fun hashCode(): Int {
        return Objects.hash(
            author,
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

    override fun toString(): String {
        return "SampleToot(id=$id, author=$author, content=$content, " +
            "publicationDateTime=$publicationDateTime, commentCount=$commentCount, " +
            "isFavorite=$isFavorite, favoriteCount=$favoriteCount, isReblogged=$isReblogged, " +
            "reblogCount=$reblogCount, url=$url)"
    }

    override suspend fun setFavorite(isFavorite: Boolean) {
        SampleTootWriter.updateFavorite(id, isFavorite)
    }

    override suspend fun setReblogged(isReblogged: Boolean) {
        SampleTootWriter.updateReblogged(id, isReblogged)
    }

    override suspend fun getComments(page: Int): Flow<List<Toot>> {
        return flowOf(emptyList())
    }
}
