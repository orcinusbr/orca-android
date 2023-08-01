package com.jeanbarrossilva.orca.core.feed.profile.type.followable

import com.jeanbarrossilva.orca.core.feed.profile.Profile

/** [Profile] whose [follow] status can be toggled. **/
abstract class FollowableProfile<T : Follow> : Profile {
    /** Current [Follow] status. **/
    abstract val follow: T

    /** Toggles the [follow] status. **/
    suspend fun toggleFollow() {
        val toggledFollow = follow.toggled()
        val matchingToggledFollow = Follow.requireVisibilityMatch(follow, toggledFollow)
        onChangeFollowTo(matchingToggledFollow)
    }

    /**
     * Callback run whenever the [Follow] status is changed to [follow].
     *
     * @param follow Changed [Follow] status.
     **/
    protected abstract suspend fun onChangeFollowTo(follow: T)

    companion object
}
