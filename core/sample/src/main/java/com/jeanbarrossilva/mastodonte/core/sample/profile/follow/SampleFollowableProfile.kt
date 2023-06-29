package com.jeanbarrossilva.mastodonte.core.sample.profile.follow

import com.jeanbarrossilva.mastodonte.core.profile.follow.Follow
import com.jeanbarrossilva.mastodonte.core.profile.follow.FollowableProfile
import com.jeanbarrossilva.mastodonte.core.profile.toot.Account
import com.jeanbarrossilva.mastodonte.core.sample.profile.SampleProfile
import com.jeanbarrossilva.mastodonte.core.sample.profile.SampleProfileDao
import java.net.URL

/**
 * [SampleProfile] that's also followable.
 *
 * @see FollowableProfile
 **/
internal abstract class SampleFollowableProfile<T : Follow> :
    SampleProfile, FollowableProfile<T>() {
    abstract override var follow: T
        internal set

    override suspend fun onChangeFollowTo(follow: T) {
        SampleProfileDao.updateFollow(id, follow)
    }

    companion object {
        /**
         * Creates an instance of a [SampleFollowableProfile].
         *
         * @param id Unique identifier.
         * @param account Unique identifier within an instance.
         * @param avatarURL [URL] that leads to the avatar image.
         * @param name Name to be displayed.
         * @param bio Describes who the owner is and/or provides information regarding this
         * [SampleFollowableProfile].
         * @param follow Current [Follow] status.
         * @param followerCount Amount of followers.
         * @param followingCount Amount of following.
         * @param url [URL] that leads to the webpage of the instance through which this
         * [SampleFollowableProfile] can be accessed.
         **/
        fun <T : Follow> createInstance(
            id: String,
            account: Account,
            avatarURL: URL,
            name: String,
            bio: String,
            follow: T,
            followerCount: Int,
            followingCount: Int,
            url: URL
        ): SampleFollowableProfile<T> {
            return object : SampleFollowableProfile<T>() {
                override val id = id
                override val account = account
                override var avatarURL = avatarURL
                override var name = name
                override var bio = bio
                override var follow = follow
                override val followerCount = followerCount
                override val followingCount = followingCount
                override val url = url
            }
        }
    }
}
