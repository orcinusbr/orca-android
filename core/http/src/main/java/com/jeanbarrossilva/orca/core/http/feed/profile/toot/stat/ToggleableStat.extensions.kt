package com.jeanbarrossilva.orca.core.http.feed.profile.toot.stat

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.toot.stat.toggleable.ToggleableStat
import com.jeanbarrossilva.orca.core.feed.profile.toot.stat.toggleable.buildToggleableStat
import com.jeanbarrossilva.orca.core.http.client.authenticateAndPost
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.HttpToot
import com.jeanbarrossilva.orca.core.http.instance.SomeHttpInstance
import com.jeanbarrossilva.orca.std.injector.Injector

/**
 * Builds a [ToggleableStat] for an [HttpToot]'s favorites that obtains them from the API.
 *
 * @param id ID of the [HttpToot] for which the [ToggleableStat] is.
 * @param count Amount of times that the [HttpToot] has been marked as favorite.
 **/
internal fun buildFavoriteStat(id: String, count: Int): ToggleableStat<Profile> {
    return buildToggleableStat(count) {
        setEnabled { isEnabled ->
            val route = if (isEnabled) {
                "/api/v1/statuses/$id/favourite"
            } else {
                @Suppress("SpellCheckingInspection")
                "/api/v1/statuses/$id/unfavourite"
            }
            Injector.get<SomeHttpInstance>().client.authenticateAndPost(route)
        }
    }
}
