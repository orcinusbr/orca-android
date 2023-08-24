package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.std.cache.Cache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MastodonTootProvider(private val cache: Cache<String, MastodonToot>) : TootProvider {
    override suspend fun provide(id: String): Flow<Toot> {
        val toot = cache.get(id)
        return flowOf(toot)
    }
}
