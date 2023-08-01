package com.jeanbarrossilva.orca.core.feed.profile.search

import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow

/** Searches for profiles through [onSearch]. **/
abstract class ProfileSearcher {
    /**
     * Searches for profiles that match the [query].
     *
     * @param query Input about the profiles being searched.
     **/
    suspend fun search(query: String): Flow<List<ProfileSearchResult>> {
        val formattedQuery = query.trim()

        @OptIn(FlowPreview::class)
        return if (formattedQuery.isBlank()) {
            emptyFlow()
        } else {
            onSearch(formattedQuery).debounce(500.milliseconds)
        }
    }

    /**
     * Searches for profiles that match the [query].
     *
     * @param query Input about the profiles being searched.
     **/
    protected abstract suspend fun onSearch(query: String): Flow<List<ProfileSearchResult>>
}
