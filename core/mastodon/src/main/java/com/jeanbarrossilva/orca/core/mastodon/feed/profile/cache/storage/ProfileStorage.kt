package com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.ProfileTootPaginateSource
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.toMastodonProfileEntity
import com.jeanbarrossilva.orca.platform.cache.Storage
import kotlinx.coroutines.flow.first

class ProfileStorage(
    private val tootPaginateSourceProvider: ProfileTootPaginateSource.Provider,
    private val entityDao: ProfileEntityDao
) : Storage<Profile>() {
    override suspend fun onStore(key: String, value: Profile) {
        val entity = value.toMastodonProfileEntity().copy(id = key)
        entityDao.insert(entity)
    }

    override suspend fun onContains(key: String): Boolean {
        return entityDao.count(key) > 0
    }

    override suspend fun onGet(key: String): Profile {
        return entityDao.getByID(key).first().toProfile(tootPaginateSourceProvider)
    }

    override suspend fun onRemove(key: String) {
        entityDao.delete(key)
    }

    override suspend fun onClear() {
        entityDao.deleteAll()
    }
}
