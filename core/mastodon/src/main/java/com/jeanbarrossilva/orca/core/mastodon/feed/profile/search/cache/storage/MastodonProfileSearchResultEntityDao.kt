package com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Performs SQL transactions regarding
 * [Mastodon profile search result entities][MastodonProfileSearchResultEntity].
 */
@Dao
internal interface MastodonProfileSearchResultEntityDao {
  /**
   * Returns the amount of
   * [Mastodon profile search result entities][MastodonProfileSearchResultEntity] that have been
   * previously found by searching with the given [query].
   *
   * @param query Query through which the
   *   [Mastodon profile search result entities][MastodonProfileSearchResultEntity] to be counted
   *   have been found.
   */
  @Query("SELECT COUNT() FROM profile_search_results WHERE `query` = :query")
  suspend fun count(query: String): Int

  /**
   * Returns a [Flow] to which the [MastodonProfileSearchResultEntity] that's been previously found
   * by searching with the given [query] will be emitted or an empty one either if none has or if no
   * [Mastodon profile search result entities][MastodonProfileSearchResultEntity] were persisted
   * with this constraint.
   *
   * @param query Query through which the [MastodonProfileSearchResultEntity] to be obtained has
   *   been found.
   */
  @Query("SELECT * FROM profile_search_results WHERE `query` = :query")
  fun selectByQuery(query: String): Flow<List<MastodonProfileSearchResultEntity>>

  /**
   * Inserts the [entities].
   *
   * @param entities [Mastodon profile search result entities][MastodonProfileSearchResultEntity] to
   *   be inserted.
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(entities: List<MastodonProfileSearchResultEntity>)

  /**
   * Deletes [Mastodon profile search result entities][MastodonProfileSearchResultEntity] that have
   * been found by searching with the given [query].
   *
   * @param query Query through which the
   *   [Mastodon profile search result entities][MastodonProfileSearchResultEntity] to be deleted
   *   have been found.
   */
  @Query("DELETE FROM profile_search_results WHERE `query` = :query")
  suspend fun delete(query: String)

  /** Deletes all [Mastodon profile search result entities][MastodonProfileSearchResultEntity]. */
  @Query("DELETE FROM profile_search_results") suspend fun deleteAll()
}
