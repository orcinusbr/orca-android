package com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache.storage

import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult

/**
 * Converts this [ProfileSearchResult] into a [ProfileSearchResultEntity].
 *
 * @param query Query with which this [ProfileSearchResult] this [ProfileSearchResult] was obtained.
 **/
internal fun ProfileSearchResult.toProfileSearchResultEntity(query: String):
        ProfileSearchResultEntity {
    return ProfileSearchResultEntity(query, id, "$account", "$avatarURL", name, "$url")
}
