package com.jeanbarrossilva.orca.core.http.feed

import com.jeanbarrossilva.orca.core.http.feed.profile.toot.HttpToot
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.pagination.HttpTootPaginateSource

/** [HttpTootPaginateSource] that paginates through [HttpToot]s of the feed. **/
internal object FeedTootPaginateSource : HttpTootPaginateSource() {
    override val route = "/api/v1/timelines/home"
}
