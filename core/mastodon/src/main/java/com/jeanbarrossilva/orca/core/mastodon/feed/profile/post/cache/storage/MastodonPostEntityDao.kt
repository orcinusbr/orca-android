package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.cache.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.style.MastodonStyleEntity
import kotlinx.coroutines.flow.Flow

/** Performs SQL transactions regarding [Mastodon post entities][MastodonPostEntity]. */
@Dao
internal interface MastodonPostEntityDao {
  /**
   * Returns the amount of [Mastodon post entities][MastodonPostEntity] identified as [id].
   *
   * @param id ID for which the counting will be performed.
   */
  @Query("SELECT COUNT() FROM posts WHERE id = :id") suspend fun count(id: String): Int

  /**
   * Returns a [Flow] to which the [MastodonPostEntity] identified as [id] will be emitted or an
   * empty one if none is found.
   *
   * @param id ID of the [MastodonPostEntity] to be obtained.
   */
  @Query("SELECT * FROM posts WHERE id = :id")
  suspend fun selectByID(id: String): MastodonPostEntity

  /**
   * Selects the [MastodonPostEntity] identified as [id] alongside its associated
   * [Mastodon style entities][MastodonStyleEntity].
   *
   * @param id ID of the [MastodonPostEntity].
   */
  @Query("SELECT * FROM posts WHERE id = :id")
  @Transaction
  suspend fun selectWithStylesByID(id: String): MastodonPostWithStyles

  /**
   * Inserts the [entity].
   *
   * @param entity [MastodonPostEntity] to be inserted.
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(entity: MastodonPostEntity)

  /**
   * Deletes the [MastodonPostEntity] identified as [id].
   *
   * @param id ID of the [MastodonPostEntity] to be deleted.
   */
  @Query("DELETE FROM posts WHERE id = :id") suspend fun delete(id: String)

  /** Deletes all [Mastodon post entities][MastodonPostEntity]. */
  @Query("DELETE FROM posts") suspend fun deleteAll()
}
