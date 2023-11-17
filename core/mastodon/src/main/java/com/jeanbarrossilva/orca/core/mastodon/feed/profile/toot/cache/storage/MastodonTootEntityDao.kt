package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.cache.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.style.MastodonStyleEntity
import kotlinx.coroutines.flow.Flow

/** Performs SQL transactions regarding [Mastodon toot entities][MastodonTootEntity]. */
@Dao
internal interface MastodonTootEntityDao {
  /**
   * Returns the amount of [Mastodon toot entities][MastodonTootEntity] identified as [id].
   *
   * @param id ID for which the counting will be performed.
   */
  @Query("SELECT COUNT() FROM toots WHERE id = :id") suspend fun count(id: String): Int

  /**
   * Returns a [Flow] to which the [MastodonTootEntity] identified as [id] will be emitted or an
   * empty one if none is found.
   *
   * @param id ID of the [MastodonTootEntity] to be obtained.
   */
  @Query("SELECT * FROM toots WHERE id = :id")
  suspend fun selectByID(id: String): MastodonTootEntity

  /**
   * Selects the [MastodonTootEntity] identified as [id] alongside its associated
   * [Mastodon style entities][MastodonStyleEntity].
   *
   * @param id ID of the [MastodonTootEntity].
   */
  @Query("SELECT * FROM toots WHERE id = :id")
  @Transaction
  suspend fun selectWithStylesByID(id: String): MastodonTootWithStyles

  /**
   * Inserts the [entity].
   *
   * @param entity [MastodonTootEntity] to be inserted.
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(entity: MastodonTootEntity)

  /**
   * Deletes the [MastodonTootEntity] identified as [id].
   *
   * @param id ID of the [MastodonTootEntity] to be deleted.
   */
  @Query("DELETE FROM toots WHERE id = :id") suspend fun delete(id: String)

  /** Deletes all [Mastodon toot entities][MastodonTootEntity]. */
  @Query("DELETE FROM toots") suspend fun deleteAll()
}
