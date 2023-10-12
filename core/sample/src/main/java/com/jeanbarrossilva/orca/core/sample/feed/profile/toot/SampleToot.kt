package com.jeanbarrossilva.orca.core.sample.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.toot.stat.Stat
import com.jeanbarrossilva.orca.core.feed.profile.toot.stat.toggleable.ToggleableStat
import java.net.URL
import java.time.ZonedDateTime

/** [Toot] whose operations are performed in memory and serves as a sample. */
internal data class SampleToot(
  override val id: String,
  override val author: Author,
  override val content: Content,
  override val publicationDateTime: ZonedDateTime,
  override val url: URL
) : Toot() {
  override val comment = Stat<Toot>()
  override val favorite = ToggleableStat<Profile>()
  override val reblog = ToggleableStat<Profile>()
}
