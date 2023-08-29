package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.test

import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.account.MastodonAccount
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.Status
import com.jeanbarrossilva.orca.core.sample.feed.profile.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.sample

/** [Status] that's returned by [sample]'s getter. **/
private val sampleStatus = Status(
    Toot.sample.id,
    "${Toot.sample.publicationDateTime}",
    MastodonAccount.sample,
    Toot.sample.reblogCount,
    Toot.sample.favoriteCount,
    Toot.sample.commentCount,
    "${Toot.sample.url}",
    "${Toot.sample.content}",
    Toot.sample.isFavorite,
    Toot.sample.isReblogged
)

/** Sample [Status]. **/
internal val Status.Companion.sample
    get() = sampleStatus
