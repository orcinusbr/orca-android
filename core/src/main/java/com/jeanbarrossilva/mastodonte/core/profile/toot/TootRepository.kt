package com.jeanbarrossilva.mastodonte.core.profile.toot

import kotlinx.coroutines.flow.Flow

/** Provides [Toot]s through various query types. **/
interface TootRepository {
    /**
     * Gets the [Toot] identified as [id].
     *
     * @param id ID of the [Toot] to be provided.
     * @return The requested [Toot], or `null` if one with such [id] doesn't exist.
     * @see Toot.id
     **/
    suspend fun get(id: String): Flow<Toot?>

    /**
     * Gets the [Toot]s made by the author whose ID equals to the given one.
     *
     * @param authorID ID of the author whose [Toot]s will be provided.
     * @return The [Toot]s associated to the author, or `null` if no author with such [authorID] is
     * found.
     **/
    suspend fun getByAuthorID(authorID: String): Flow<List<Toot>?>
}
