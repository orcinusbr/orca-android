package com.jeanbarrossilva.orca.core.http.feed.profile.toot.test

import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.http.feed.profile.account.HttpAccount
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.status.HttpAttachment
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.status.HttpCard
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.status.HttpStatus
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.sample

/** [HttpStatus] returned by [sample]. **/
private val sampleHttpStatus = HttpStatus(
    Toot.sample.id,
    createdAt = "${Toot.sample.publicationDateTime}",
    HttpAccount.sample,
    Toot.sample.reblogCount,
    Toot.sample.favoriteCount,
    repliesCount = Toot.sample.commentCount,
    "${Toot.sample.url}",
    reblog = null,
    HttpCard.sample,
    "${Toot.sample.content.text}",
    HttpAttachment.samples,
    Toot.sample.isFavorite,
    Toot.sample.isReblogged
)

/** Sample [HttpStatus]. **/
internal val HttpStatus.Companion.sample
    get() = sampleHttpStatus
