package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.cache.storage.mention

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class MentionEntityDao internal constructor() {
    @Query("SELECT * FROM mentions WHERE toot_id = :tootID")
    internal abstract suspend fun selectByTootID(tootID: String): List<MentionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    internal abstract suspend fun insert(entities: List<MentionEntity>)

    @Delete
    internal abstract suspend fun remove(entities: List<MentionEntity>)
}
