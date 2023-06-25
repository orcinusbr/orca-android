package com.jeanbarrossilva.mastodon.feature.profile.ui

import com.jeanbarrossilva.mastodonte.core.profile.follow.Follow

/**
 * Describes this status or the action that involves toggling this [Follow] status.
 *
 * @see Follow.toggled
 **/
val Follow.label
    get() = when (this) {
        Follow.Public.unfollowed(), Follow.Private.unfollowed() -> "Follow"
        Follow.Private.requested() -> "Requested"
        else -> "Unfollow"
    }
