package com.jeanbarrossilva.mastodonte.core.mastodon.feed.profile.type.followable

import com.jeanbarrossilva.mastodonte.core.feed.profile.Profile
import com.jeanbarrossilva.mastodonte.core.feed.profile.type.followable.Follow

/**
 * Gets the route to which a call would equate to this [Follow]'s [toggled][Follow.toggled] state.
 *
 * @param profile [Profile] to which this [Follow] is related.
 **/
internal fun Follow.getToggledRoute(profile: Profile): String {
    return when (this) {
        Follow.Public.following(), Follow.Private.requested(), Follow.Private.following() ->
            "/api/v1/accounts/${profile.id}/unfollow"
        else ->
            "/api/v1/accounts/${profile.id}/follow"
    }
}
