package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.cache.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Headline
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.MastodonToot
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.cache.storage.style.startingAt
import com.jeanbarrossilva.orca.platform.cache.Cache
import com.jeanbarrossilva.orca.std.styledstring.toStyledString
import java.net.URL
import java.time.ZonedDateTime

@Entity(tableName = "toots")
internal data class MastodonTootEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "author_id") val authorID: String,
    val text: String,
    @ColumnInfo(name = "headline_title") val headlineTitle: String?,
    @ColumnInfo(name = "headline_subtitle") val headlineSubtitle: String?,
    @ColumnInfo(name = "headline_cover_url") val headlineCoverURL: String?,
    @ColumnInfo(name = "publication_date_time") val publicationDateTime: String,
    @ColumnInfo(name = "comment_count") val commentCount: Int,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean,
    @ColumnInfo(name = "favorite_count") val favoriteCount: Int,
    @ColumnInfo(name = "is_reblogged") val isReblogged: Boolean,
    @ColumnInfo(name = "reblog_count") val reblogCount: Int,
    @ColumnInfo(name = "url") val url: String
) {
    suspend fun toMastodonToot(
        profileCache: Cache<Profile>,
        dao: MastodonTootEntityDao
    ): MastodonToot {
        val author = profileCache.get(authorID).toAuthor()
        val mentions = dao.selectWithStylesByID(id).mentions
        val text = text.toStyledString { URL(mentions.startingAt(it).url) }
        val content = Content.from(text) {
            if (headlineTitle != null && headlineCoverURL != null) {
                Headline(headlineTitle, headlineSubtitle, URL(headlineCoverURL))
            } else {
                null
            }
        }
        val publicationDateTime = ZonedDateTime.parse(publicationDateTime)
        val url = URL(url)
        return MastodonToot(
            id,
            author,
            content,
            publicationDateTime,
            commentCount,
            isFavorite,
            favoriteCount,
            isReblogged,
            reblogCount,
            url
        )
    }

    companion object {
        fun from(toot: MastodonToot): MastodonTootEntity {
            return MastodonTootEntity(
                toot.id,
                toot.author.id,
                "${toot.content.text}",
                toot.content.highlight?.headline?.title,
                toot.content.highlight?.headline?.subtitle,
                "${toot.content.highlight?.headline?.coverURL}",
                "${toot.publicationDateTime}",
                toot.commentCount,
                toot.isFavorite,
                toot.favoriteCount,
                toot.isReblogged,
                toot.reblogCount,
                "${toot.url}"
            )
        }
    }
}
