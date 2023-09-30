package com.jeanbarrossilva.orca.core.http.feed.profile.toot.test

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.orca.core.http.feed.profile.account.HttpAccount
import com.jeanbarrossilva.orca.core.sample.feed.profile.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.sample

/** [HttpAccount] returned by [sample]. **/
private val sampleHttpAccount = HttpAccount(
    Profile.sample.id,
    Profile.sample.account.username,
    "${Profile.sample.account}",
    "${Profile.sample.url}",
    displayName = Profile.sample.name,
    locked = (Profile.sample as? FollowableProfile<*>)?.follow is Follow.Private,
    note = "${Profile.sample.bio}",
    "${Profile.sample.avatarURL}",
    Toot.sample.favoriteCount,
    Toot.sample.reblogCount
)

/** Sample [HttpAccount]. **/
internal val HttpAccount.Companion.sample
    get() = sampleHttpAccount
