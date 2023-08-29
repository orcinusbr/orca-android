package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.test

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.account.MastodonAccount
import com.jeanbarrossilva.orca.core.sample.feed.profile.sample

/** [MastodonAccount] that's returned by [sample]'s getter. **/
private val sampleMastodonAccount = MastodonAccount(
    Profile.sample.id,
    Profile.sample.account.username,
    "${Profile.sample.account}",
    "${Profile.sample.url}",
    Profile.sample.name,
    locked = false,
    Profile.sample.bio,
    "${Profile.sample.avatarURL}",
    Profile.sample.followerCount,
    Profile.sample.followingCount
)

/** Sample [MastodonAccount]. **/
internal val MastodonAccount.Companion.sample
    get() = sampleMastodonAccount
