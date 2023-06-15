package com.jeanbarrossilva.mastodonte.core.profile

import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import java.net.URL

/** [Profile] whose [Follow] status type is not taken into consideration. **/
typealias AnyProfile = Profile<*>

/** A user's profile. **/
abstract class Profile<T : Follow> protected constructor() {
    /** Unique identifier. **/
    abstract val id: String

    /** Unique identifier within an instance. **/
    abstract val account: Account

    /** [URL] that leads to the avatar image. **/
    abstract val avatarURL: URL

    /** Name to be displayed. **/
    abstract val name: String

    /** Describes who the owner is and/or provides information regarding this [Profile]. **/
    abstract val bio: String

    /** Current [Follow] status. **/
    abstract val follow: T

    /** Amount of followers. **/
    abstract val followerCount: Int

    /** Amount of following. **/
    abstract val followingCount: Int

    /**
     * [URL] that leads to the webpage of the instance through which this [Profile] can be accessed.
     **/
    abstract val url: URL

    /** Toggles the [follow] status. **/
    suspend fun toggleFollow() {
        val toggledFollow = follow.toggled()
        val matchingToggledFollow = Follow.requireVisibilityMatch(follow, toggledFollow)
        onChangeFollowTo(matchingToggledFollow)
    }

    abstract suspend fun getToots(page: Int): List<Toot>

    /**
     * Callback run whenever the [Follow] status is changed to [follow].
     *
     * @param follow Changed [Follow] status.
     **/
    protected abstract suspend fun onChangeFollowTo(follow: T)
}
