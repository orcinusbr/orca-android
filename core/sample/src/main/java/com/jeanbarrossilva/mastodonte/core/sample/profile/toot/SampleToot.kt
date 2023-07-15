package com.jeanbarrossilva.mastodonte.core.sample.profile.toot

import com.jeanbarrossilva.mastodonte.core.profile.toot.Author
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import java.net.URL
import java.time.ZonedDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/** [Toot] whose operations are performed in memory and serves as a sample. **/
internal data class SampleToot(
    override val id: String,
    override val author: Author,
    override val content: String,
    override val publicationDateTime: ZonedDateTime,
    override val commentCount: Int,
    override val isFavorite: Boolean,
    override val favoriteCount: Int,
    override val reblogCount: Int,
    override val url: URL
) : Toot() {
    override suspend fun setFavorite(isFavorite: Boolean) {
        SampleTootWriter.updateFavorite(id, isFavorite)
    }

    override suspend fun getComments(page: Int): Flow<List<Toot>> {
        return flowOf(emptyList())
    }
}
