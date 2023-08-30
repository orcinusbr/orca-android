package com.jeanbarrossilva.orca.core.mastodon

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.ProfileEntity
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.ProfileEntityDao
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache.storage.ProfileSearchResultEntity
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache.storage.ProfileSearchResultEntityDao
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.cache.storage.MastodonTootEntity
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.cache.storage.MastodonTootEntityDao
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.cache.storage.mention.StyleEntity
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.cache.storage.mention.StyleEntityDao

@Database(
    entities = [
        StyleEntity::class,
        ProfileEntity::class,
        ProfileSearchResultEntity::class,
        MastodonTootEntity::class
    ],
    version = 1
)
abstract class MastodonDatabase internal constructor() : RoomDatabase() {
    abstract val styleEntityDao: StyleEntityDao
    abstract val profileEntityDao: ProfileEntityDao
    abstract val profileSearchResultEntityDao: ProfileSearchResultEntityDao
    abstract val tootEntityDao: MastodonTootEntityDao

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
