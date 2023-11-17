package com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache.storage

import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult

/**
 * Converts this [ProfileSearchResult] into a [MastodonProfileSearchResultEntity].
 *
 * @param query Query with which this [ProfileSearchResult] was obtained.
 */
internal fun ProfileSearchResult.toMastodonProfileSearchResultEntity(
  query: String
): MastodonProfileSearchResultEntity {
  return MastodonProfileSearchResultEntity(query, id, "$account", "$avatarLoader", name, "$url")
}
