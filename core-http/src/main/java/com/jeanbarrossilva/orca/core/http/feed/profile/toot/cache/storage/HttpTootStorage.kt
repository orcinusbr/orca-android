package com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.HttpToot
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage.style.HttpStyleEntity
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage.style.HttpStyleEntityDao
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage.style.toHttpStyleEntity
import com.jeanbarrossilva.orca.platform.cache.Cache
import com.jeanbarrossilva.orca.platform.cache.Storage

/**
 * [Storage] for [HttpToot]s.
 *
 * @param profileCache [Cache] of [Profile]s with which [HTTP toot entities][HttpTootEntity] will be
 * converted to [HttpToot]s.
 * @param tootEntityDao [HttpStyleEntityDao] that will perform SQL transactions on
 * [HTTP toot entities][HttpTootEntity].
 * @param styleEntityDao [HttpStyleEntityDao] for inserting and deleting
 * [HTTP style entities][HttpStyleEntity].
 **/
class HttpTootStorage(
    private val profileCache: Cache<Profile>,
    private val tootEntityDao: HttpTootEntityDao,
    private val styleEntityDao: HttpStyleEntityDao
) : Storage<HttpToot>() {
    override suspend fun onStore(key: String, value: HttpToot) {
        val tootEntity = HttpTootEntity.from(value)
        val styleEntities = value.content.text.styles.map { it.toHttpStyleEntity(value.id) }
        tootEntityDao.insert(tootEntity)
        styleEntityDao.insert(styleEntities)
    }

    override suspend fun onContains(key: String): Boolean {
        return tootEntityDao.count(key) > 0
    }

    override suspend fun onGet(key: String): HttpToot {
        return tootEntityDao.selectByID(key).toHttpToot(profileCache, tootEntityDao)
    }

    override suspend fun onRemove(key: String) {
        val mentionEntities = styleEntityDao.selectByTootID(key)
        styleEntityDao.delete(mentionEntities)
        tootEntityDao.delete(key)
    }

    override suspend fun onClear() {
        tootEntityDao.deleteAll()
    }
}
