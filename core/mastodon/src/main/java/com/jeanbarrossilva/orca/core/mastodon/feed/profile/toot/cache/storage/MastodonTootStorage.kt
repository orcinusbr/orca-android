package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.cache.storage

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.MastodonToot
import com.jeanbarrossilva.orca.std.cache.Cache
import com.jeanbarrossilva.orca.std.cache.Storage

class MastodonTootStorage(
    private val profileCache: Cache<String, Profile>,
    private val entityDao: MastodonTootEntityDao
) : Storage<String, MastodonToot>() {
    override suspend fun onStore(key: String, value: MastodonToot) {
        val entity = MastodonTootEntity.from(value)
        entityDao.insert(entity)
    }

    override suspend fun onContains(key: String): Boolean {
        return entityDao.count(key) > 0
    }

    override suspend fun onGet(key: String): MastodonToot {
        return entityDao.selectByID(key).toMastodonToot(profileCache)
    }

    override suspend fun onRemove(key: String) {
        entityDao.delete(key)
    }

    override suspend fun onClear() {
        entityDao.deleteAll()
    }
}
