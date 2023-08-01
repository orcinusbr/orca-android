package com.jeanbarrossilva.orca.core.feed.profile.search

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow

/** Searches for [Profile]s through [onSearch]. **/
abstract class ProfileSearcher {
    /**
     * Searches for [Profile]s that match the [query].
     *
     * @param query Input about the [Profile]s being searched.
     **/
    suspend fun search(query: String): Flow<List<Profile>> {
        val formattedQuery = query.trim()

        @OptIn(FlowPreview::class)
        return if (formattedQuery.isBlank()) {
            emptyFlow()
        } else {
            onSearch(formattedQuery).debounce(500.milliseconds)
        }
    }

    /**
     * Searches for [Profile]s that match the [query].
     *
     * @param query Input about the [Profile]s being searched.
     **/
    protected abstract suspend fun onSearch(query: String): Flow<List<Profile>>
}
