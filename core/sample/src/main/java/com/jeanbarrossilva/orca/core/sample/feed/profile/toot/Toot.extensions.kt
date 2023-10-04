package com.jeanbarrossilva.orca.core.sample.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.content.sample
import java.net.URL
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

/** [Toot] returned by [sample]'s getter. **/
private val sampleToot = createSample()

/** [Toot]s returned by [samples]' getter. **/
private val sampleToots = listOf(sampleToot) + List(31) { createSample() }

/** A sample [Toot]. **/
val Toot.Companion.sample: Toot
    get() = sampleToot

/**
 * Sample [Toot]s.
 **/
val Toot.Companion.samples
    get() = sampleToots

/** Creates a sample [Toot]. **/
private fun createSample(): Toot {
    return SampleToot(
        "${UUID.randomUUID()}",
        Author.sample,
        Content.sample,
        publicationDateTime = ZonedDateTime.of(2_003, 10, 8, 8, 0, 0, 0, ZoneId.of("GMT-3")),
        URL("https://mastodon.social/@christianselig/110492858891694580")
    )
}
