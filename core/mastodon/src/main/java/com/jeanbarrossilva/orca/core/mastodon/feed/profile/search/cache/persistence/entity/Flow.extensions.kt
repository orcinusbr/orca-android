package com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache.persistence.entity

import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** Maps each emitted [List] of profile search result entities to a [ProfileSearchResult]. **/
internal fun Flow<List<ProfileSearchResultEntity>>.mapToProfileSearchResults():
    Flow<List<ProfileSearchResult>> {
    return map { entities ->
        entities.map { entity ->
            entity.toProfileSearchResult()
        }
    }
}
