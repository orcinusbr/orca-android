package com.jeanbarrossilva.mastodonte.core.profile

import kotlinx.coroutines.flow.Flow

/** Provides [Profile]s through various query types. **/
fun interface ProfileRepository {
    /**
     * Gets the [Profile] identified as [id].
     *
     * @param id ID of the [Profile] to be provided.
     * @return The requested [Profile], or `null` if one with such [id] doesn't exist.
     * @see Profile.id
     **/
    suspend fun get(id: String): Flow<AnyProfile?>
}
