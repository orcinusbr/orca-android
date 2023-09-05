package com.jeanbarrossilva.orca.core.sample.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import java.net.URL
import java.time.ZonedDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/** [Toot] whose operations are performed in memory and serves as a sample. **/
internal data class SampleToot(
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
