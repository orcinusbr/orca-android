package com.jeanbarrossilva.orca.core.http.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.stat.CommentStat
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.stat.FavoriteStat
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.stat.ReblogStat
import io.ktor.client.request.HttpRequest
import java.net.URL
import java.time.ZonedDateTime

/**
 * [Toot] whose actions perform an [HttpRequest] and communicate with the Mastodon API.
 *
 * @param commentCount Amount of comments that this [HttpToot] has received.
 * @param favoriteCount Amount of times that this [HttpToot] has been marked as favorite.
 * @param reblogCount Amount of times that this [HttpToot] has been reblogged.
 **/
data class HttpToot internal constructor(
    override val id: String,
    override val author: Author,
    override val content: Content,
    override val publicationDateTime: ZonedDateTime,
    private val commentCount: Int,
    private val favoriteCount: Int,
    private val reblogCount: Int,
    override val url: URL
) : Toot() {
    override val comment = CommentStat(id, commentCount)
    override val favorite = FavoriteStat(id, favoriteCount)
    override val reblog = ReblogStat(id, reblogCount)
}
