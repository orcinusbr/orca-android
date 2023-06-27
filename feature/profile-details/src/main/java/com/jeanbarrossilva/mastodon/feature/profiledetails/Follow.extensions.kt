package com.jeanbarrossilva.mastodon.feature.profiledetails

import com.jeanbarrossilva.mastodonte.core.profile.follow.Follow

/** Converts this [Follow] into a [ProfileDetails.Followable.Follow]. **/
internal fun Follow.toHeaderFollow(): ProfileDetails.Followable.Follow {
    return when (this) {
        Follow.Public.following(), Follow.Private.following() -> ProfileDetails.Followable.Follow.FOLLOWING
        Follow.Private.requested() -> ProfileDetails.Followable.Follow.REQUESTED
        else -> ProfileDetails.Followable.Follow.UNFOLLOWED
    }
}
