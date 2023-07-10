package com.jeanbarrossilva.mastodon.feature.profiledetails.conversion.converter.followable

import com.jeanbarrossilva.mastodon.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.mastodonte.core.profile.follow.Follow

/** Converts this [Follow] into a [ProfileDetails.Followable.Status]. **/
internal fun Follow.toStatus(): ProfileDetails.Followable.Status {
    return when (this) {
        Follow.Public.following(), Follow.Private.following() ->
            ProfileDetails.Followable.Status.FOLLOWING
        Follow.Private.requested() ->
            ProfileDetails.Followable.Status.REQUESTED
        else ->
            ProfileDetails.Followable.Status.UNFOLLOWED
    }
}
