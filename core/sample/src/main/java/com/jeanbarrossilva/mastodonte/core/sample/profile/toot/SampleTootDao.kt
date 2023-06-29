package com.jeanbarrossilva.mastodonte.core.sample.profile.toot

import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import com.jeanbarrossilva.mastodonte.core.profile.toot.TootRepository
import com.jeanbarrossilva.mastodonte.core.sample.profile.SampleProfileDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

/** Central for all [Toot]-related reading and writing operations. **/
object SampleTootDao : TootRepository {
    /** [MutableStateFlow] that provides the [Toot]s. **/
    private val tootsFlow = MutableStateFlow(Toot.samples)

    override suspend fun get(id: String): Flow<Toot?> {
        return tootsFlow.map { toots ->
            toots.find { toot ->
                toot.id == id
            }
        }
    }

    override suspend fun getByAuthorID(authorID: String): Flow<List<Toot>?> {
        return if (SampleProfileDao.contains(authorID)) {
            getByExistingAuthorID(authorID)
        } else {
            flowOf(null)
        }
    }

    /**
     * Gets the [Toot]s made by the author whose ID equals to the given one. It's specifically
     * intended for when the presence of such author has already been checked, since it'll return an
     * idle [Flow] if it doesn't exist.
     *
     * @param authorID ID of the author whose [Toot]s will be provided.
     **/
    private fun getByExistingAuthorID(authorID: String): Flow<List<Toot>> {
        return tootsFlow.map { toots ->
            toots.filter { toot ->
                toot.author.id == authorID
            }
        }
    }
}
