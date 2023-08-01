package com.jeanbarrossilva.orca.core.sample.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

/** [TootProvider] that provides sample [Toot]s. **/
object SampleTootProvider : TootProvider {
    /** [Toot]s that are present by default. **/
    internal val defaultToots = Toot.samples

    /** [MutableStateFlow] that provides the [Toot]s. **/
    internal val tootsFlow = MutableStateFlow(defaultToots)

    /** [IllegalArgumentException] thrown if a nonexistent [Author]'s [Toot]s are requested. **/
    class NonexistentAuthorException internal constructor(id: String) :
        IllegalArgumentException("Author identified as \"$id\" doesn't exist.")

    /** [IllegalArgumentException] thrown if a nonexistent [Toot] is requested. **/
    class NonexistentTootException internal constructor(id: String) :
        IllegalArgumentException("Toot identified as \"$id\" doesn't exist.")

    override suspend fun provide(id: String): Flow<Toot> {
        return tootsFlow.mapNotNull { toots ->
            toots.find { toot ->
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
