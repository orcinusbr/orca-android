package com.jeanbarrossilva.orca.core.mastodon.feed

import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.status.TootPaginateSource

object FeedTootPaginateSource : TootPaginateSource() {
    override val route = "/api/v1/timelines/home"
}
