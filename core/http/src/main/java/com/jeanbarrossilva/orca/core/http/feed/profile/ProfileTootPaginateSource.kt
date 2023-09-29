package com.jeanbarrossilva.orca.core.http.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.pagination.HttpTootPaginateSource

/**
 * [HttpTootPaginateSource] that paginates through an [HttpProfile]'s [Toot]s.
 *
 * @param id ID of the [HttpProfile].
 **/
internal class ProfileTootPaginateSource(id: String) : HttpTootPaginateSource() {
    override val route = "/api/v1/accounts/$id/statuses"

    /** Provides a [ProfileTootPaginateSource] through [provide]. **/
    fun interface Provider {
        /**
         * Provides a [ProfileTootPaginateSource] that paginates through the [Toot]s of an
         * [HttpProfile] identified as [id].
         *
         * id ID of the [HttpProfile].
         **/
        fun provide(id: String): ProfileTootPaginateSource
    }
}
