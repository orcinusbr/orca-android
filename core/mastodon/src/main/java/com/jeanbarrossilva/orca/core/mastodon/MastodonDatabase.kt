package com.jeanbarrossilva.orca.core.mastodon

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.MastodonProfileEntity
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.MastodonProfileEntityDao
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.style.MastodonStyleEntity
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.style.MastodonStyleEntityDao
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.cache.storage.MastodonPostEntity
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.cache.storage.MastodonPostEntityDao
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache.storage.MastodonProfileSearchResultEntity
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache.storage.MastodonProfileSearchResultEntityDao

/** [RoomDatabase] in which core-Mastodon-related persistence operations will take place. */
@Database(
  entities =
    [
      MastodonStyleEntity::class,
      MastodonProfileEntity::class,
      MastodonProfileSearchResultEntity::class,
      MastodonPostEntity::class
    ],
  version = 1
)
internal abstract class MastodonDatabase : RoomDatabase() {
  /** DAO for operating on [Mastodon style entities][MastodonStyleEntity]. */
  abstract val styleEntityDao: MastodonStyleEntityDao

  /** DAO for operating on [Mastodon profile entities][MastodonProfileEntity]. */
  abstract val profileEntityDao: MastodonProfileEntityDao

  /**
   * DAO for operating on
   * [Mastodon profile search result entities][MastodonProfileSearchResultEntity].
   */
  abstract val profileSearchResultEntityDao: MastodonProfileSearchResultEntityDao

  /** DAO for operating on [Mastodon post entities][MastodonPostEntity]. */
  abstract val postEntityDao: MastodonPostEntityDao

  companion object {
    private lateinit var instance: MastodonDatabase

    /**
     * Builds or retrieves the previously instantiated [MastodonDatabase].
     *
     * @param context [Context] to be used for building it.
     */
    fun getInstance(context: Context): MastodonDatabase {
      return if (Companion::instance.isInitialized) {
        instance
      } else {
        instance = build(context)
        instance
      }
    }

    /**
     * Builds a [MastodonDatabase].
     *
     * @param context [Context] from which it will be built.
     */
    private fun build(context: Context): MastodonDatabase {
      return Room.databaseBuilder(context, MastodonDatabase::class.java, "mastodon-database")
        .build()
    }
  }
}
