package com.jeanbarrossilva.mastodonte.core.profile

import kotlinx.coroutines.flow.Flow

/** Provides a [Profile] through [onProvide]. **/
abstract class ProfileProvider {
    /**
     * [IllegalArgumentException] thrown when a [Profile] that doesn't exist is requested to be
     * provided.
     *
     * @param id ID of the [Profile] requested to be provided.
     **/
    class NonexistentProfileException internal constructor(id: String) : IllegalArgumentException(
        "Profile identified as \"$id\" doesn't exist."
    )

    /**
     * Gets the [Profile] identified as [id].
     *
     * @param id ID of the [Profile] to be provided.
     * @see Profile.id
     **/
    suspend fun provide(id: String): Flow<Profile> {
        return if (contains(id)) onProvide(id) else throw NonexistentProfileException(id)
    }

    /**
     * Whether a [Profile] identified as [id] exists.
     *
     * @param id ID of the [Profile] whose existence will be checked.
     **/
    protected abstract suspend fun contains(id: String): Boolean

    /**
     * Gets the [Profile] identified as [id].
     *
     * @param id ID of the [Profile] to be provided.
     * @see Profile.id
     **/
    protected abstract suspend fun onProvide(id: String): Flow<Profile>
}
