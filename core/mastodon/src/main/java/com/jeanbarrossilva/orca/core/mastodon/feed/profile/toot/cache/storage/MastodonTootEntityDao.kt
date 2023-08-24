package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.cache.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class MastodonTootEntityDao internal constructor() {
    @Query("SELECT * FROM toots WHERE id = :id")
    internal abstract suspend fun selectByID(id: String): MastodonTootEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    internal abstract suspend fun insert(entity: MastodonTootEntity)

    @Query("SELECT COUNT() FROM toots WHERE id = :id")
    internal abstract suspend fun count(id: String): Int

    @Query("DELETE FROM toots WHERE id = :id")
    internal abstract suspend fun delete(id: String)

    @Query("DELETE FROM toots")
    internal abstract suspend fun deleteAll()
}
