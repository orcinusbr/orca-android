package com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage

import com.jeanbarrossilva.orca.cache.Storage
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.toMastodonProfileEntity
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.status.TootPaginateSource
import kotlinx.coroutines.flow.first

class ProfileStorage(
    private val tootPaginateSource: TootPaginateSource,
    private val entityDao: ProfileEntityDao
) : Storage<String, Profile>() {
    override suspend fun onContains(key: String): Boolean {
        return entityDao.count(key) > 0
    }

    override suspend fun onStore(key: String, value: Profile) {
        val entity = value.toMastodonProfileEntity().copy(id = key)
        entityDao.insert(entity)
    }

    override suspend fun onGet(key: String): Profile {
        return entityDao.getByID(key).first().toProfile(tootPaginateSource)
    }

    override suspend fun onRemove(key: String) {
        entityDao.delete(key)
    }

    override suspend fun onClear() {
        entityDao.deleteAll()
    }
}
