package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.cache.storage.mention

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mentions")
internal data class MentionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "toot_id") val tootID: String,
    @ColumnInfo(name = "start_index") val startIndex: Int,
    @ColumnInfo(name = "end_index") val endIndex: Int,
    @ColumnInfo(name = "url") val url: String
)
