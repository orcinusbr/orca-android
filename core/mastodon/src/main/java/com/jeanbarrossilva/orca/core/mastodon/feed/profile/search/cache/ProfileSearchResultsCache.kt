package com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache

import com.jeanbarrossilva.orca.cache.Cache
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache.storage.ProfileSearchResultsStorage

class ProfileSearchResultsCache(
    override val fetcher: ProfileSearchResultsFetcher,
    override val storage: ProfileSearchResultsStorage
) : Cache<String, List<ProfileSearchResult>>()
