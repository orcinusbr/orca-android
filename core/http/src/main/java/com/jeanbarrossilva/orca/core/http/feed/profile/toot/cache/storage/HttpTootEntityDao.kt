package com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage.style.HttpStyleEntity
import kotlinx.coroutines.flow.Flow

/** Performs SQL transactions regarding [HTTP toot entities][HttpTootEntity]. */
@Dao
internal interface HttpTootEntityDao {
  /**
   * Returns the amount of [HTTP toot entities][HttpTootEntity] identified as [id].
   *
   * @param id ID for which the counting will be performed.
   */
  @Query("SELECT COUNT() FROM toots WHERE id = :id") suspend fun count(id: String): Int

  /**
   * Returns a [Flow] to which the [HttpTootEntity] identified as [id] will be emitted or an empty
   * one if none is found.
   *
   * @param id ID of the [HttpTootEntity] to be obtained.
   */
  @Query("SELECT * FROM toots WHERE id = :id") suspend fun selectByID(id: String): HttpTootEntity

  /**
   * Returns a [Flow] to which the [HttpTootEntity] identified as [id] alongside its associated
   * [HTTP style entities][HttpStyleEntity] will be emitted or an empty one if none is found.
   *
   * @param id ID of the [HttpTootEntity].
   */
  @Query("SELECT * FROM toots WHERE id = :id")
  @Transaction
  suspend fun selectWithStylesByID(id: String): HttpTootWithStyles

  /**
   * Inserts the [entity].
   *
   * @param entity [HttpTootEntity] to be inserted.
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(entity: HttpTootEntity)

  /**
   * Deletes the [HttpTootEntity] identified as [id].
   *
   * @param id ID of the [HttpTootEntity] to be deleted.
   */
  @Query("DELETE FROM toots WHERE id = :id") suspend fun delete(id: String)

  /** Deletes all [HTTP toot entities][HttpTootEntity]. */
  @Query("DELETE FROM toots") suspend fun deleteAll()
}
