package com.jeanbarrossilva.orca.core.http.feed.profile.search.cache.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Performs SQL transactions regarding
 * [HTTP profile search result entities][HttpProfileSearchResultEntity].
 */
@Dao
internal interface HttpProfileSearchResultEntityDao {
  /**
   * Returns the amount of [HTTP profile search result entities][HttpProfileSearchResultEntity] that
   * have been previously found by searching with the given [query].
   *
   * @param query Query through which the
   *   [HTTP profile search result entities][HttpProfileSearchResultEntity] to be counted have been
   *   found.
   */
  @Query("SELECT COUNT() FROM profile_search_results WHERE `query` = :query")
  suspend fun count(query: String): Int

  /**
   * Returns a [Flow] to which the [HttpProfileSearchResultEntity] that's been previously found by
   * searching with the given [query] will be emitted or an empty one either if none has or if no
   * [HTTP profile search result entities][HttpProfileSearchResultEntity] were persisted with this
   * constraint.
   *
   * @param query Query through which the [HttpProfileSearchResultEntity] to be obtained has been
   *   found.
   */
  @Query("SELECT * FROM profile_search_results WHERE `query` = :query")
  fun selectByQuery(query: String): Flow<List<HttpProfileSearchResultEntity>>

  /**
   * Inserts the [entities].
   *
   * @param entities [HTTP profile search result entities][HttpProfileSearchResultEntity] to be
   *   inserted.
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(entities: List<HttpProfileSearchResultEntity>)

  /**
   * Deletes [HTTP profile search result entities][HttpProfileSearchResultEntity] that have been
   * found by searching with the given [query].
   *
   * @param query Query through which the
   *   [HTTP profile search result entities][HttpProfileSearchResultEntity] to be deleted have been
   *   found.
   */
  @Query("DELETE FROM profile_search_results WHERE `query` = :query")
  suspend fun delete(query: String)

  /** Deletes all [HTTP profile search result entities][HttpProfileSearchResultEntity]. */
  @Query("DELETE FROM profile_search_results") suspend fun deleteAll()
}
