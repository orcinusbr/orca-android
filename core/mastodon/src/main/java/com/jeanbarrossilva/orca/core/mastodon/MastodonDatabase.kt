package com.jeanbarrossilva.orca.core.mastodon

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.ProfileEntity
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.ProfileEntityDao
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache.storage.ProfileSearchResultEntity
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache.storage.ProfileSearchResultEntityDao

@Database(entities = [ProfileEntity::class, ProfileSearchResultEntity::class], version = 1)
abstract class MastodonDatabase internal constructor() : RoomDatabase() {
    abstract val profileEntityDao: ProfileEntityDao
    abstract val profileSearchResultEntityDao: ProfileSearchResultEntityDao

    companion object {
        private lateinit var instance: MastodonDatabase

        fun getInstance(context: Context): MastodonDatabase {
            return if (Companion::instance.isInitialized) {
                instance
            } else {
                instance = build(context)
                instance
            }
        }

        private fun build(context: Context): MastodonDatabase {
            return Room
                .databaseBuilder(context, MastodonDatabase::class.java, "mastodon-database")
                .build()
        }
    }
}
