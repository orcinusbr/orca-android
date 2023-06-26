package com.jeanbarrossilva.mastodonte.core.inmemory.profile.toot

import com.jeanbarrossilva.mastodonte.core.profile.toot.Author
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import java.net.URL
import java.time.ZonedDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/** [Toot] whose operations are performed in memory. **/
data class InMemoryToot(
    override val id: String,
    override val author: Author,
    override val content: String,
    override val publicationDateTime: ZonedDateTime,
    override val commentCount: Int,
    override val favoriteCount: Int,
    override val reblogCount: Int,
    override val url: URL
) : Toot() {
    override suspend fun getComments(page: Int): Flow<List<Toot>> {
        return flowOf(emptyList())
    }
}
