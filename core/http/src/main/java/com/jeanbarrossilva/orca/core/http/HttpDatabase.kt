package com.jeanbarrossilva.orca.core.http

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jeanbarrossilva.orca.core.http.feed.profile.cache.storage.HttpProfileEntity
import com.jeanbarrossilva.orca.core.http.feed.profile.cache.storage.HttpProfileEntityDao
import com.jeanbarrossilva.orca.core.http.feed.profile.cache.storage.style.HttpStyleEntity
import com.jeanbarrossilva.orca.core.http.feed.profile.cache.storage.style.HttpStyleEntityDao
import com.jeanbarrossilva.orca.core.http.feed.profile.search.cache.storage.HttpProfileSearchResultEntity
import com.jeanbarrossilva.orca.core.http.feed.profile.search.cache.storage.HttpProfileSearchResultEntityDao
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage.HttpTootEntity
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage.HttpTootEntityDao

/** [RoomDatabase] in which core-HTTP-related persistence operations will take place. */
@Database(
  entities =
    [
      HttpStyleEntity::class,
      HttpProfileEntity::class,
      HttpProfileSearchResultEntity::class,
      HttpTootEntity::class
    ],
  version = 1
)
internal abstract class HttpDatabase : RoomDatabase() {
  /** DAO for operating on [HTTP style entities][HttpStyleEntity]. */
  abstract val styleEntityDao: HttpStyleEntityDao

  /** DAO for operating on [HTTP profile entities][HttpProfileEntity]. */
  abstract val profileEntityDao: HttpProfileEntityDao

  /** DAO for operating on [HTTP profile search result entities][HttpProfileSearchResultEntity]. */
  abstract val profileSearchResultEntityDao: HttpProfileSearchResultEntityDao

  /** DAO for operating on [HTTP toot entities][HttpTootEntity]. */
  abstract val tootEntityDao: HttpTootEntityDao

  companion object {
    private lateinit var instance: HttpDatabase

    /**
     * Builds or retrieves the previously instantiated [HttpDatabase].
     *
     * @param context [Context] to be used for building it.
     */
    fun getInstance(context: Context): HttpDatabase {
      return if (Companion::instance.isInitialized) {
        instance
      } else {
        instance = build(context)
        instance
      }
    }

    /**
     * Builds a [HttpDatabase].
     *
     * @param context [Context] from which it will be built.
     */
    private fun build(context: Context): HttpDatabase {
      return Room.databaseBuilder(context, HttpDatabase::class.java, "http-database").build()
    }
  }
}
