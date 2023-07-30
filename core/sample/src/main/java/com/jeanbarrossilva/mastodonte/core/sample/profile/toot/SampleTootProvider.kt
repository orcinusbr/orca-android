package com.jeanbarrossilva.mastodonte.core.sample.profile.toot

import com.jeanbarrossilva.mastodonte.core.profile.toot.Author
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import com.jeanbarrossilva.mastodonte.core.profile.toot.TootProvider
import com.jeanbarrossilva.mastodonte.core.sample.profile.SampleProfileProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/** [TootProvider] that provides sample [Toot]s. **/
object SampleTootProvider : TootProvider {
    /** [IllegalArgumentException] thrown if a nonexistent [Author]'s [Toot]s are requested. **/
    class NonexistentAuthorException internal constructor(id: String) :
        IllegalArgumentException("Author identified as \"$id\" doesn't exist.")

    /** [IllegalArgumentException] thrown if a nonexistent [Toot] is requested. **/
    class NonexistentTootException internal constructor(id: String) :
        IllegalArgumentException("Toot identified as \"$id\" doesn't exist.")

    /** [MutableStateFlow] that provides the [Toot]s. **/
    internal val tootsFlow = MutableStateFlow(Toot.samples)

    override suspend fun provide(id: String): Flow<Toot> {
        return tootsFlow.map { toots ->
            toots.first { toot ->
                toot.id == id
            }
        }
    }

    /**
     * Provides the [Toot]s made by the author whose ID equals to the given one.
     *
     * @param authorID ID of the author whose [Toot]s will be provided.
     **/
    internal suspend fun provideBy(authorID: String): Flow<List<Toot>> {
        return if (SampleProfileProvider.contains(authorID)) {
            tootsFlow.map { toots ->
                toots.filter { toot ->
                    toot.author.id == authorID
                }
            }
        } else {
            throw NonexistentAuthorException(authorID)
        }
    }
}
