package com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache.storage

import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.platform.cache.Storage
import kotlinx.coroutines.flow.first

class ProfileSearchResultsStorage(private val entityDao: ProfileSearchResultEntityDao) :
    Storage<String, List<ProfileSearchResult>>() {
    override suspend fun onStore(key: String, value: List<ProfileSearchResult>) {
        val entities = value.map { it.toProfileSearchResultEntity(key) }
        entityDao.insert(entities)
    }

    override suspend fun onContains(key: String): Boolean {
        return entityDao.count(key) > 0
    }

    override suspend fun onGet(key: String): List<ProfileSearchResult> {
        return entityDao.selectByQuery(key).first().map(
            ProfileSearchResultEntity::toProfileSearchResult
        )
    }

    override suspend fun onRemove(key: String) {
        entityDao.delete(key)
    }

    override suspend fun onClear() {
        entityDao.deleteAll()
    }
}
