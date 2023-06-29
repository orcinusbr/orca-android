package com.jeanbarrossilva.mastodonte.core.sample.profile.toot

import com.jeanbarrossilva.mastodonte.core.profile.toot.Author
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import java.net.URL
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

/** [Toot] returned by [sample]'s getter. **/
@Suppress("SpellCheckingInspection")
private val sampleToot = SampleToot(
    "${UUID.randomUUID()}",
    Author.sample,
    content = "<p>This is a <b>sample</b> <i>toot</i> that has the sole purpose of allowing " +
        "one to see how it would look like in <u>Mastodonte</u>.</p>",
    publicationDateTime = ZonedDateTime.of(2_003, 10, 8, 8, 0, 0, 0, ZoneId.of("GMT-3")),
    commentCount = 1_024,
    favoriteCount = 2_048,
    reblogCount = 512,
    URL("https://mastodon.social/@christianselig/110492858891694580")
)

/** [Toot]s returned by [samples]' getter. **/
private val sampleToots = List<Toot>(32) {
    sampleToot
}

/** A sample [Toot]. **/
val Toot.Companion.sample: Toot
    get() = sampleToot

/**
 * Sample [Toot]s.
 **/
val Toot.Companion.samples
    get() = sampleToots
