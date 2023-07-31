package com.jeanbarrossilva.mastodonte.core.mastodon.feed.profile.cache.persistence.entity

import com.jeanbarrossilva.mastodonte.core.feed.profile.Profile
import com.jeanbarrossilva.mastodonte.core.mastodon.feed.profile.toot.status.TootPaginateSource
import java.util.Optional
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Returns a [Flow] with each emitted [MastodonProfileEntity] converted into a [Profile].
 *
 * @param tootPaginateSource [TootPaginateSource] to convert the entities with.
 * @see MastodonProfileEntity.toProfile
 **/
internal fun Flow<MastodonProfileEntity?>.mapToProfileOptional(
    tootPaginateSource: TootPaginateSource
): Flow<Optional<Profile>> {
    return map {
        val profile = it?.toProfile(tootPaginateSource)
        Optional.ofNullable(profile)
    }
}
