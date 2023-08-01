package com.jeanbarrossilva.orca.core.mastodon.feed.profile.search

import com.dropbox.android.external.store4.get
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.loadable.flow.loadableFlow
import com.jeanbarrossilva.loadable.flow.unwrap
import com.jeanbarrossilva.loadable.list.SerializableList
import com.jeanbarrossilva.loadable.list.toSerializableList
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearcher
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache.ProfileSearchResultsStore
import kotlinx.coroutines.flow.Flow

class MastodonProfileSearcher(private val store: ProfileSearchResultsStore) : ProfileSearcher() {
    private val searchResultsFlow = loadableFlow<SerializableList<ProfileSearchResult>>()

    override suspend fun onSearch(query: String): Flow<List<ProfileSearchResult>> {
        val searchResults = store.get(query).toSerializableList()
        searchResultsFlow.value = Loadable.Loaded(searchResults)
        return searchResultsFlow.unwrap()
    }
}
