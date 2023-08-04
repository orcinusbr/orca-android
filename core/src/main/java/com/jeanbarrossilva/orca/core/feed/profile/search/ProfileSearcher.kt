package com.jeanbarrossilva.orca.core.feed.profile.search

import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf

/** Searches for profiles through [search]. **/
abstract class ProfileSearcher {
    /** [MutableStateFlow] to which the query is emitted. **/
    private val queryFlow = MutableStateFlow("")

    /** [Flow] with an empty [List] of [ProfileSearchResult]s. **/
    private val emptyResultsFlow = flowOf(emptyList<ProfileSearchResult>())

    /**
     * [MutableStateFlow] to which [ProfileSearchResult]s based on the query are emitted.
     *
     * @see queryFlow
     **/
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val resultsFlow = queryFlow.debounce(256.milliseconds).flatMapMerge {
        if (it.isBlank()) emptyResultsFlow else onSearch(it)
    }

    /**
     * Searches for profiles that match the [query].
     *
     * @param query Input about the profiles being searched.
     **/
    fun search(query: String): Flow<List<ProfileSearchResult>> {
        queryFlow.value = query.trim()
        return resultsFlow
    }

    /**
     * Searches for profiles that match the [query].
     *
     * @param query Input about the profiles being searched.
     **/
    protected abstract suspend fun onSearch(query: String): Flow<List<ProfileSearchResult>>
}
