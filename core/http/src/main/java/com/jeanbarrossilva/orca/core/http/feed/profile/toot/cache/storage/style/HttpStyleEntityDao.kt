package com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage.style

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import kotlinx.coroutines.flow.Flow

/** Performs SQL transactions regarding [HTTP style entities][HttpStyleEntity]. **/
@Dao
internal interface HttpStyleEntityDao {
    /**
     * Returns a [Flow] to which the [HttpStyleEntity] whose [Toot]'s identified as [tootID] will be
     * emitted or an empty one if none is found.
     *
     * @param tootID ID of the [Toot] to which the [HttpStyleEntity] to be obtained belongs.
     **/
    @Query("SELECT * FROM styles WHERE toot_id = :tootID")
    suspend fun selectByTootID(tootID: String): List<HttpStyleEntity>

    /**
     * Inserts the [HTTP style entities][HttpStyleEntity].
     *
     * @param entities [HTTP style entities][HttpStyleEntity] to be inserted.
     **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<HttpStyleEntity>)

    /**
     * Deletes the [HTTP style entities][HttpStyleEntity].
     *
     * @param entities [HTTP style entities][HttpStyleEntity] to be deleted.
     **/
    @Delete
    suspend fun delete(entities: List<HttpStyleEntity>)
}
