package com.jeanbarrossilva.orca.core.http.feed.profile.cache.storage.style

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/** Performs SQL transactions regarding [HTTP style entities][HttpStyleEntity]. */
@Dao
internal interface HttpStyleEntityDao {
  /**
   * Returns a [Flow] to which the [HttpStyleEntity] whose parents are identified as [parentID] will
   * be emitted or an empty one if none is found.
   *
   * @param parentID ID of the parent to which the [HttpStyleEntity] to be obtained belongs.
   */
  @Query("SELECT * FROM styles WHERE parent_id = :parentID")
  suspend fun selectByParentID(parentID: String): List<HttpStyleEntity>

  /**
   * Inserts the [HTTP style entities][HttpStyleEntity].
   *
   * @param entities [HTTP style entities][HttpStyleEntity] to be inserted.
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(entities: List<HttpStyleEntity>)

  /**
   * Deletes the [HTTP style entities][HttpStyleEntity].
   *
   * @param entities [HTTP style entities][HttpStyleEntity] to be deleted.
   */
  @Delete suspend fun delete(entities: List<HttpStyleEntity>)
}
