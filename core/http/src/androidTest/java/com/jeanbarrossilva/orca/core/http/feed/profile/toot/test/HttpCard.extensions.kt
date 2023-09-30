package com.jeanbarrossilva.orca.core.http.feed.profile.toot.test

import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.status.HttpCard
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.content.highlight.sample

/** [HttpCard] returned by [sample]. **/
private val sampleHttpCard = HttpCard(
    "${Highlight.sample.url}",
    Highlight.sample.headline.title,
    Highlight.sample.headline.subtitle.orEmpty(),
    "${Highlight.sample.headline.coverURL}"
)

/** Sample [HttpCard]. **/
internal val HttpCard.Companion.sample
    get() = sampleHttpCard
