package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.core.http.authenticateAndGet
import com.jeanbarrossilva.orca.core.http.authenticateAndPost
import com.jeanbarrossilva.orca.core.mastodon.client.MastodonHttpClient
import com.jeanbarrossilva.orca.core.mastodon.get
import io.ktor.client.call.body
import java.net.URL
import java.time.ZonedDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class MastodonToot(
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
        @Suppress("SpellCheckingInspection")
        val route =
            if (isFavorite) "/api/v1/statuses/$id/favourite" else "/api/v1/statuses/$id/unfavourite"

        MastodonHttpClient.authenticateAndPost(authenticationLock = get(), route)
    }

    override suspend fun setReblogged(isReblogged: Boolean) {
        @Suppress("SpellCheckingInspection")
        val route =
            if (isReblogged) "/api/v1/statuses/:id/reblog" else "/api/v1/statuses/:id/unreblog"

        MastodonHttpClient.authenticateAndPost(authenticationLock = get(), route)
    }

    override suspend fun getComments(page: Int): Flow<List<Toot>> {
        return flow {
            MastodonHttpClient
                .authenticateAndGet(authenticationLock = get(), "/api/v1/statuses/$id/context")
                .body<Context>()
                .descendants
                .map { it.toToot() }
                .also { emit(it) }
        }
    }
}
