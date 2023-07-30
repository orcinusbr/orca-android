package com.jeanbarrossilva.mastodonte.feature.profiledetails.conversion.converter.followable

import com.jeanbarrossilva.mastodonte.core.profile.type.followable.Follow
import com.jeanbarrossilva.mastodonte.feature.profiledetails.ProfileDetails

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
