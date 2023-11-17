package com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.style

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
