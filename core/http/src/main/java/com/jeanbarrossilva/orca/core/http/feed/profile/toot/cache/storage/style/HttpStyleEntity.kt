package com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage.style

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.std.styledstring.Style
import com.jeanbarrossilva.orca.std.styledstring.type.Link

/**
 * Primitive information to be stored about a [Style].
 *
 * @param id Automatically generated unique identifier.
 * @param tootID ID of the [Toot] to which the [Style] belongs.
 * @param startIndex Indicates the first index in the [Toot]'s [content][Toot.content]'s
 * [text][Content.text] to which the [Style] has been applied.
 * @param endIndex Final position in the [Toot]'s [content][Toot.content]'s [text][Content.text]
 * that has the [Style].
 * @param url URL [String] to which the styled portion leads if it happens to be a [Link] or `null`
 * if it isn't.
 **/
@Entity(tableName = "styles")
internal data class HttpStyleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "toot_id") val tootID: String,
    @ColumnInfo(name = "start_index") val startIndex: Int,
    @ColumnInfo(name = "end_index") val endIndex: Int,
    @ColumnInfo(name = "url") val url: String?
)
