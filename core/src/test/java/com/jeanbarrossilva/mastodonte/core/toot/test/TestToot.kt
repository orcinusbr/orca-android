package com.jeanbarrossilva.mastodonte.core.toot.test

import com.jeanbarrossilva.mastodonte.core.sample.account.sample
import com.jeanbarrossilva.mastodonte.core.toot.Author
import com.jeanbarrossilva.mastodonte.core.toot.Toot
import java.net.URL
import java.time.ZonedDateTime
import kotlinx.coroutines.flow.Flow

/** Local [Toot] that defaults its properties' values to [Toot.Companion.sample]'s. **/
internal class TestToot(
    override val id: String = Toot.sample.id,
    override val author: Author = Toot.sample.author,
    override val content: String = Toot.sample.content,
    override val publicationDateTime: ZonedDateTime = Toot.sample.publicationDateTime,
    override val commentCount: Int = Toot.sample.commentCount,
    isLiked: Boolean = Toot.sample.isFavorite,
    override val favoriteCount: Int = Toot.sample.favoriteCount,
    isReblogged: Boolean = Toot.sample.isReblogged,
    override val reblogCount: Int = Toot.sample.reblogCount,
    override val url: URL = Toot.sample.url
) : Toot() {
    override var isFavorite = isLiked
        private set
    override var isReblogged = isReblogged
        private set

    override suspend fun setFavorite(isFavorite: Boolean) {
        this.isFavorite = isFavorite
    }

    override suspend fun setReblogged(isReblogged: Boolean) {
        this.isReblogged = isReblogged
    }

    override suspend fun getComments(page: Int): Flow<List<Toot>> {
        return Toot.sample.getComments(page)
    }
}
