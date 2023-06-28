package com.jeanbarrossilva.mastodon.feature.profiledetails

import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.edit.EditableProfile
import com.jeanbarrossilva.mastodonte.core.profile.follow.FollowableProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Converts this core [ProfileDetails] into [ProfileDetails].
 *
 * @param coroutineScope [CoroutineScope] through which suspending operations, such as editing or
 * follow-toggling, will be performed.
 **/
internal fun Profile.toProfileDetails(coroutineScope: CoroutineScope): ProfileDetails {
    return when (this) {
        is EditableProfile ->
            ProfileDetails.Editable(id, avatarURL, name, account, bio, url)
        is FollowableProfile<*> ->
            ProfileDetails.Followable(
                id,
                avatarURL,
                name,
                account,
                bio,
                url,
                follow.toStatus(),
                onStatusToggle = {
                    coroutineScope.launch {
                        toggleFollow()
                    }
                }
            )
        else ->
            ProfileDetails.Default(id, avatarURL, name, account, bio, url)
    }
}
