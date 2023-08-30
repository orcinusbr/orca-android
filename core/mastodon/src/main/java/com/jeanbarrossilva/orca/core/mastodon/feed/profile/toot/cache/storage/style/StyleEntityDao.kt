package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.cache.storage.style

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class StyleEntityDao internal constructor() {
    @Query("SELECT * FROM styles WHERE toot_id = :tootID")
    internal abstract suspend fun selectByTootID(tootID: String): List<StyleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    internal abstract suspend fun insert(entities: List<StyleEntity>)

    @Delete
    internal abstract suspend fun remove(entities: List<StyleEntity>)
}
