package com.jeanbarrossilva.orca.core.http.feed.profile.cache.storage

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.http.feed.profile.HttpProfile
import com.jeanbarrossilva.orca.core.http.feed.profile.ProfileTootPaginateSource
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.HttpToot
import com.jeanbarrossilva.orca.platform.cache.Storage
import kotlinx.coroutines.flow.first

/**
 * [Storage] for [Profile]s.
 *
 * @param tootPaginateSourceProvider [ProfileTootPaginateSource.Provider] by which a
 * [ProfileTootPaginateSource] for paginating through a [HttpProfile]'s [HttpToot]s will be
 * provided.
 * @param entityDao [HttpProfileEntityDao] that will perform SQL transactions on
 * [HTTP profile entities][HttpProfileEntity].
 **/
class HttpProfileStorage(
    private val tootPaginateSourceProvider: ProfileTootPaginateSource.Provider,
    private val entityDao: HttpProfileEntityDao
) : Storage<Profile>() {
    override suspend fun onStore(key: String, value: Profile) {
        val entity = value.toMastodonProfileEntity().copy(id = key)
        entityDao.insert(entity)
    }

    override suspend fun onContains(key: String): Boolean {
        return entityDao.count(key) > 0
    }

    override suspend fun onGet(key: String): Profile {
        return entityDao.selectByID(key).first().toProfile(tootPaginateSourceProvider)
    }

    override suspend fun onRemove(key: String) {
        entityDao.delete(key)
    }

    override suspend fun onClear() {
        entityDao.deleteAll()
    }
}
