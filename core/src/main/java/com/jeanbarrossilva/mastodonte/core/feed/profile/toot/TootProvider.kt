package com.jeanbarrossilva.mastodonte.core.feed.profile.toot

import kotlinx.coroutines.flow.Flow

/** Provides [Toot]s. **/
interface TootProvider {
    /**
     * Provides the [Toot] identified as [id].
     *
     * @param id ID of the [Toot] to be provided.
     * @see Toot.id
     **/
    suspend fun provide(id: String): Flow<Toot>
}
