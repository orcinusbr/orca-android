package com.jeanbarrossilva.mastodonte.core.mastodon.feed.profile.cache.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jeanbarrossilva.mastodonte.core.mastodon.feed.profile.cache.persistence.entity.MastodonProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MastodonProfileEntityDao {
    @Query("SELECT * FROM profiles WHERE id = :id LIMIT 1")
    internal abstract fun getByID(id: String): Flow<MastodonProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    internal abstract suspend fun insert(entity: MastodonProfileEntity)

    @Query("DELETE FROM profiles WHERE id = :id")
    internal abstract suspend fun delete(id: String)

    @Query("DELETE FROM profiles")
    internal abstract suspend fun deleteAll()
}
