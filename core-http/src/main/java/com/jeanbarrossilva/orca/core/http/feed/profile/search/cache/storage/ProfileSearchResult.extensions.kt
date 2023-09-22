package com.jeanbarrossilva.orca.core.http.feed.profile.search.cache.storage

import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult

/**
 * Converts this [ProfileSearchResult] into an [HttpProfileSearchResultEntity].
 *
 * @param query Query with which this [ProfileSearchResult] was obtained.
 **/
internal fun ProfileSearchResult.toProfileSearchResultEntity(query: String):
    HttpProfileSearchResultEntity {
    return HttpProfileSearchResultEntity(query, id, "$account", "$avatarURL", name, "$url")
}
