package com.jeanbarrossilva.orca.core.feed.profile.account.reblog

import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.reblog.Reblog
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.reblog.sample
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal class ReblogTests {
    @Test
    fun createsReblog() {
        assertEquals(
            object : Reblog() {
                override val id = Reblog.sample.id
                override val author = Reblog.sample.author
                override val reblogger = Reblog.sample.reblogger
                override val content = Reblog.sample.content
                override val publicationDateTime = Reblog.sample.publicationDateTime
                override val commentCount = Reblog.sample.commentCount
                override val isFavorite = Reblog.sample.isFavorite
                override val favoriteCount = Reblog.sample.favoriteCount
                override val isReblogged = Reblog.sample.isReblogged
                override val reblogCount = Reblog.sample.reblogCount
                override val url = Reblog.sample.url

                override suspend fun getComments(page: Int): Flow<List<Toot>> {
                    return emptyFlow()
                }

                override suspend fun setFavorite(isFavorite: Boolean) {
                }

                override suspend fun setReblogged(isReblogged: Boolean) {
                }
            },
            Reblog(
                Reblog.sample.id,
                Reblog.sample.author,
                Reblog.sample.reblogger,
                Reblog.sample.content,
                Reblog.sample.publicationDateTime,
                Reblog.sample.commentCount,
                Reblog.sample.isFavorite,
                Reblog.sample.favoriteCount,
                Reblog.sample.isReblogged,
                Reblog.sample.reblogCount,
                Reblog.sample.url
            )
        )
    }

    @Test
    fun createsReblogFromOriginalToot() {
        assertEquals(Reblog.sample, Reblog(Reblog.sample, Reblog.sample.reblogger))
    }
}
