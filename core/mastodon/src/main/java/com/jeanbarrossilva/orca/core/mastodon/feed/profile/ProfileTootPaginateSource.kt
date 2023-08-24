package com.jeanbarrossilva.orca.core.mastodon.feed.profile

import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.status.TootPaginateSource

class ProfileTootPaginateSource(id: String) : TootPaginateSource() {
    override val route = "/api/v1/accounts/$id/statuses"

    fun interface Provider {
        fun provide(id: String): ProfileTootPaginateSource
    }
}
