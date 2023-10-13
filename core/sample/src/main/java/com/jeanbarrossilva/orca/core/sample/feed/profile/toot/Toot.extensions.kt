package com.jeanbarrossilva.orca.core.sample.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.toot.reblog.Reblog
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.content.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.reblog.sample
import java.net.URL
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

/** [Toot] returned by [sample]'s getter. */
private val sampleToot = SampleToot(
  "${UUID.randomUUID()}",
  Author.sample,
  Content.sample,
  publicationDateTime = ZonedDateTime.of(2_003, 10, 8, 8, 0, 0, 0, ZoneId.of("GMT-3")),
  URL("https://mastodon.social/@christianselig/110492858891694580")
)

/** [Toot]s returned by [samples]' getter. */
private val sampleToots = listOf(Reblog.sample, sampleToot)

/** A sample [Toot]. */
val Toot.Companion.sample: Toot
  get() = sampleToots.first()

/** Sample [Toot]s. */
val Toot.Companion.samples
  get() = sampleToots
