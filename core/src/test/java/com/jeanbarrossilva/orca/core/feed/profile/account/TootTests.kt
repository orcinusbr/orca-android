package com.jeanbarrossilva.orca.core.feed.profile.account

import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.toot.Boost
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.sample
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TootTests {
    @Test
    fun convertsIntoBoost() {
        val booster = Author.sample.copy(id = "🥸")
        assertEquals(
            object : Boost() {
                override val id = Toot.sample.id
                override val author = Toot.sample.author
                override val booster = booster
                override val content = Toot.sample.content
                override val publicationDateTime = Toot.sample.publicationDateTime
                override val commentCount = Toot.sample.commentCount
                override val isFavorite = Toot.sample.isFavorite
                override val favoriteCount = Toot.sample.favoriteCount
                override val isReblogged = Toot.sample.isReblogged
                override val reblogCount = Toot.sample.reblogCount
                override val url = Toot.sample.url

                override suspend fun getComments(page: Int): Flow<List<Toot>> {
                    return emptyFlow()
                }

                override suspend fun setFavorite(isFavorite: Boolean) {
                }

                override suspend fun setReblogged(isReblogged: Boolean) {
                }
            },
            Toot.sample.toBoost(booster)
        )
    }
}
