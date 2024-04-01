/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.core.mastodon.feed.profile.cache.storage.style

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/** Performs SQL transactions regarding [Mastodon style entities][MastodonStyleEntity]. */
@Dao
internal interface MastodonStyleEntityDao {
  /**
   * Returns a [Flow] to which the [MastodonStyleEntity] whose parents are identified as [parentID]
   * will be emitted or an empty one if none is found.
   *
   * @param parentID ID of the parent to which the [MastodonStyleEntity] to be obtained belongs.
   */
  @Query("SELECT * FROM styles WHERE parent_id = :parentID")
  suspend fun selectByParentID(parentID: String): List<MastodonStyleEntity>

  /**
   * Inserts the [Mastodon style entities][MastodonStyleEntity].
   *
   * @param entities [Mastodon style entities][MastodonStyleEntity] to be inserted.
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(entities: List<MastodonStyleEntity>)

  /**
   * Deletes the [Mastodon style entities][MastodonStyleEntity].
   *
   * @param entities [Mastodon style entities][MastodonStyleEntity] to be deleted.
   */
  @Delete suspend fun delete(entities: List<MastodonStyleEntity>)
}
