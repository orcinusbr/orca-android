package com.jeanbarrossilva.orca.core.http.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.core.http.client.authenticateAndGet
import com.jeanbarrossilva.orca.core.http.client.authenticateAndPost
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.status.HttpStatus
import com.jeanbarrossilva.orca.core.http.get
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequest
import java.net.URL
import java.time.ZonedDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/** [Toot] whose actions perform an [HttpRequest] and communicate with the Mastodon API. **/
data class HttpToot(
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
        val route =
            @Suppress("SpellCheckingInspection")
            if (isFavorite) "/api/v1/statuses/$id/favourite" else "/api/v1/statuses/$id/unfavourite"

        get<HttpClient>().authenticateAndPost(authenticationLock = get(), route)
    }

    override suspend fun setReblogged(isReblogged: Boolean) {
        val route =
            @Suppress("SpellCheckingInspection")
            if (isReblogged) "/api/v1/statuses/:id/reblog" else "/api/v1/statuses/:id/unreblog"

        get<HttpClient>().authenticateAndPost(authenticationLock = get(), route)
    }

    override suspend fun getComments(page: Int): Flow<List<Toot>> {
        return flow {
            get<HttpClient>()
                .authenticateAndGet(authenticationLock = get(), "/api/v1/statuses/$id/context")
                .body<HttpContext>()
                .descendants
                .map(HttpStatus::toToot)
                .also { emit(it) }
        }
    }
}
