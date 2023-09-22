package com.jeanbarrossilva.orca.core.http

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jeanbarrossilva.orca.core.http.feed.profile.cache.storage.HttpProfileEntity
import com.jeanbarrossilva.orca.core.http.feed.profile.cache.storage.HttpProfileEntityDao
import com.jeanbarrossilva.orca.core.http.feed.profile.search.cache.storage.HttpProfileSearchResultEntity
import com.jeanbarrossilva.orca.core.http.feed.profile.search.cache.storage.HttpProfileSearchResultEntityDao
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage.HttpTootEntity
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage.HttpTootEntityDao
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage.style.HttpStyleEntity
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage.style.HttpStyleEntityDao

/** [RoomDatabase] in which core-HTTP-related persistence operations will take place. **/
@Database(
    entities = [
        HttpStyleEntity::class,
        HttpProfileEntity::class,
        HttpProfileSearchResultEntity::class,
        HttpTootEntity::class
    ],
    version = 1
)
abstract class MastodonDatabase internal constructor() : RoomDatabase() {
    /** DAO for operating on [HTTP style entities][HttpStyleEntity]. **/
    abstract val styleEntityDao: HttpStyleEntityDao

    /** DAO for operating on [HTTP profile entities][HttpProfileEntity]. **/
    abstract val profileEntityDao: HttpProfileEntityDao

    /**
     * DAO for operating on [HTTP profile search result entities][HttpProfileSearchResultEntity].
     **/
    abstract val profileSearchResultEntityDao: HttpProfileSearchResultEntityDao

    /** DAO for operating on [HTTP toot entities][HttpTootEntity]. **/
    abstract val tootEntityDao: HttpTootEntityDao

    companion object {
        private lateinit var instance: MastodonDatabase

        /**
         * Builds or retrieves the previously instantiated [MastodonDatabase].
         *
         * @param context [Context] to be used for building it.
         **/
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
         **/
        private fun build(context: Context): MastodonDatabase {
            return Room
                .databaseBuilder(context, MastodonDatabase::class.java, "mastodon-database")
                .build()
        }
    }
}
