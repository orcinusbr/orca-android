package com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache.persistence.entity.ProfileSearchResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProfileSearchResultEntityDao internal constructor() {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    internal abstract suspend fun insert(entities: List<ProfileSearchResultEntity>)

    @Query("SELECT * FROM profile_search_results WHERE `query` = :query")
    internal abstract fun selectByQuery(query: String): Flow<List<ProfileSearchResultEntity>>

    @Query("DELETE FROM profile_search_results WHERE `query` = :query")
    internal abstract suspend fun delete(query: String)

    @Query("DELETE FROM profile_search_results")
    internal abstract suspend fun deleteAll()
}
