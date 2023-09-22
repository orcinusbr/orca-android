package com.jeanbarrossilva.orca.core.http.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.platform.cache.Cache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * [TootProvider] that either requests [HttpToot]s to the API or retrieves cached ones if they're
 * available.
 *
 * @param cache [Cache] of [HttpToot]s by which [HttpToot]s will be obtained.
 **/
class HttpTootProvider internal constructor(private val cache: Cache<HttpToot>) : TootProvider {
    override suspend fun provide(id: String): Flow<HttpToot> {
        val toot = cache.get(id)
        return flowOf(toot)
    }
}
