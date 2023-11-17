package com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

/** Performs SQL transactions regarding [Mastodon profile entities][MastodonProfileEntity]. */
@Dao
internal interface MastodonProfileEntityDao {
  /**
   * Returns the amount of [Mastodon profile entities][MastodonProfileEntity] identified as [id].
   *
   * @param id ID for which the counting will be performed.
   */
  @Query("SELECT COUNT(id) FROM profiles WHERE id = :id") suspend fun count(id: String): Int

  /**
   * Returns a [Flow] to which the [MastodonProfileEntity] identified as [id] will be emitted or an
   * empty one if none is found.
   *
   * @param id ID of the [MastodonProfileEntity] to be obtained.
   */
  @Query("SELECT * FROM profiles WHERE id = :id LIMIT 1")
  fun selectByID(id: String): Flow<MastodonProfileEntity>

  /**
   * Selects the [MastodonProfileEntity] identified as [id] alongside the
   * [Mastodon style entities][HttpStyleEntity] associated to its [bio][MastodonProfileEntity.bio].
   *
   * @param id ID of the [MastodonProfileEntity].
   */
  @Query("SELECT * FROM profiles WHERE id = :id")
  @Transaction
  suspend fun selectWithBioStylesByID(id: String): MastodonProfileWithBioStyles

  /**
   * Inserts the [entity].
   *
   * @param entity [MastodonProfileEntity] to be inserted.
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(entity: MastodonProfileEntity)

  /**
   * Deletes the [MastodonProfileEntity] identified as [id].
   *
   * @param id ID of the [MastodonProfileEntity] to be deleted.
   */
  @Query("DELETE FROM profiles WHERE id = :id") suspend fun delete(id: String)

  /** Deletes all [Mastodon profile entities][MastodonProfileEntity]. */
  @Query("DELETE FROM profiles") suspend fun deleteAll()
}
