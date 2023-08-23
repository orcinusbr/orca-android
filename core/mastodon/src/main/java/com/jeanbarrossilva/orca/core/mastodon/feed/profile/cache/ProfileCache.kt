package com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache

import com.jeanbarrossilva.orca.cache.Cache
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.ProfileStorage

class ProfileCache(override val fetcher: ProfileFetcher, override val storage: ProfileStorage) :
    Cache<String, Profile>()
