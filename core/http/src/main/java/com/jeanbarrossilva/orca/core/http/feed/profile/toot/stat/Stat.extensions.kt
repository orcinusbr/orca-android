package com.jeanbarrossilva.orca.core.http.feed.profile.toot.stat

import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.stat.Stat
import com.jeanbarrossilva.orca.core.feed.profile.toot.stat.buildStat
import com.jeanbarrossilva.orca.core.http.client.authenticateAndGet
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.HttpContext
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.HttpToot
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.status.HttpStatus
import com.jeanbarrossilva.orca.core.http.instance.SomeHttpInstance
import com.jeanbarrossilva.orca.std.injector.Injector
import io.ktor.client.call.body
import kotlinx.coroutines.flow.flow

/**
 * Builds a [Stat] for an [HttpToot]'s comments that obtains them from the API.
 *
 * @param id ID of the [HttpToot] for which the [Stat] is.
 * @param count Amount of comments that the [HttpToot] has received.
 **/
internal fun buildCommentStat(id: String, count: Int): Stat<Toot> {
    return buildStat(count) {
        get {
            flow {
                Injector
                    .get<SomeHttpInstance>()
                    .client
                    .authenticateAndGet("/api/v1/statuses/$id/context")
                    .body<HttpContext>()
                    .descendants
                    .map(HttpStatus::toToot)
                    .also { emit(it) }
            }
        }
    }
}
