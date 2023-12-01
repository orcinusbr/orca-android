/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

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
