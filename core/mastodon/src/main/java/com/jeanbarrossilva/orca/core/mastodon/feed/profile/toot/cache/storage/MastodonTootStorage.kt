package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.cache.storage

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.MastodonToot
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.cache.storage.mention.StyleEntityDao
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.cache.storage.mention.toMentionEntity
import com.jeanbarrossilva.orca.platform.cache.Cache
import com.jeanbarrossilva.orca.platform.cache.Storage

class MastodonTootStorage(
    private val profileCache: Cache<Profile>,
    private val tootEntityDao: MastodonTootEntityDao,
    private val styleEntityDao: StyleEntityDao
) : Storage<MastodonToot>() {
    override suspend fun onStore(key: String, value: MastodonToot) {
        val tootEntity = MastodonTootEntity.from(value)
        val styleEntities = value.content.styles.map { it.toMentionEntity(value.id) }
        tootEntityDao.insert(tootEntity)
        styleEntityDao.insert(styleEntities)
    }

    override suspend fun onContains(key: String): Boolean {
        return tootEntityDao.count(key) > 0
    }

    override suspend fun onGet(key: String): MastodonToot {
        return tootEntityDao.selectByID(key).toMastodonToot(profileCache, tootEntityDao)
    }

    override suspend fun onRemove(key: String) {
        val mentionEntities = styleEntityDao.selectByTootID(key)
        styleEntityDao.remove(mentionEntities)
        tootEntityDao.delete(key)
    }

    override suspend fun onClear() {
        tootEntityDao.deleteAll()
    }
}
