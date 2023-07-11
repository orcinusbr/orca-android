package com.jeanbarrossilva.mastodonte.core.sample.profile.toot

import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import com.jeanbarrossilva.mastodonte.core.profile.toot.TootRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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
        return tootsFlow.map { toots ->
            toots.filter { toot ->
                toot.author.id == authorID
            }
        }
    }
}
