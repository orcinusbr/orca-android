package com.jeanbarrossilva.mastodonte.core.sample.profile.type.follow

import com.jeanbarrossilva.mastodonte.core.account.Account
import com.jeanbarrossilva.mastodonte.core.profile.type.follow.Follow
import com.jeanbarrossilva.mastodonte.core.profile.type.follow.FollowableProfile
import com.jeanbarrossilva.mastodonte.core.sample.profile.SampleProfile
import com.jeanbarrossilva.mastodonte.core.sample.profile.SampleProfileWriter
import java.net.URL

/**
 * [SampleProfile] that's also followable.
 *
 * @see FollowableProfile
 **/
internal data class SampleFollowableProfile<T : Follow>(
    override val id: String,
    override val account: Account,
    override val avatarURL: URL,
    override val name: String,
    override val bio: String,
    override val follow: T,
    override val followerCount: Int,
    override val followingCount: Int,
    override val url: URL
) : SampleProfile, FollowableProfile<T>() {
    override suspend fun onChangeFollowTo(follow: T) {
        SampleProfileWriter.updateFollow(id, follow)
    }
}
