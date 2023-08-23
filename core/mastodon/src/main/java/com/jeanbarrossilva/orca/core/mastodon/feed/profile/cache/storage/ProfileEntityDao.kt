package com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProfileEntityDao {
    @Query("SELECT COUNT(id) FROM profiles WHERE id = :id")
    internal abstract suspend fun count(id: String): Int

    @Query("SELECT * FROM profiles WHERE id = :id LIMIT 1")
    internal abstract fun getByID(id: String): Flow<ProfileEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    internal abstract suspend fun insert(entity: ProfileEntity)

    @Query("DELETE FROM profiles WHERE id = :id")
    internal abstract suspend fun delete(id: String)

    @Query("DELETE FROM profiles")
    internal abstract suspend fun deleteAll()
}
