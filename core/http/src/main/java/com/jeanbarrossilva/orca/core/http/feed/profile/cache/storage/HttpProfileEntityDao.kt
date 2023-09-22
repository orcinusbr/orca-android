package com.jeanbarrossilva.orca.core.http.feed.profile.cache.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/** Performs SQL transactions regarding [HTTP profile entities][HttpProfileEntity]. **/
@Dao
abstract class HttpProfileEntityDao {
    /**
     * Returns the amount of [HTTP profile entities][HttpProfileEntity] identified as [id].
     *
     * @param id ID for which the counting will be performed.
     **/
    @Query("SELECT COUNT(id) FROM profiles WHERE id = :id")
    internal abstract suspend fun count(id: String): Int

    /**
     * Returns a [Flow] to which the [HttpProfileEntity] identified as [id] will be emitted or an
     * empty one if none is found.
     *
     * @param id ID of the [HttpProfileEntity] to be obtained.
     **/
    @Query("SELECT * FROM profiles WHERE id = :id LIMIT 1")
    internal abstract fun selectByID(id: String): Flow<HttpProfileEntity>

    /**
     * Inserts the [entity].
     *
     * @param entity [HttpProfileEntity] to be inserted.
     **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    internal abstract suspend fun insert(entity: HttpProfileEntity)

    /**
     * Deletes the [HttpProfileEntity] identified as [id].
     *
     * @param id ID of the [HttpProfileEntity] to be deleted.
     **/
    @Query("DELETE FROM profiles WHERE id = :id")
    internal abstract suspend fun delete(id: String)

    /** Deletes all [HTTP profile entities][HttpProfileEntity]. **/
    @Query("DELETE FROM profiles")
    internal abstract suspend fun deleteAll()
}
