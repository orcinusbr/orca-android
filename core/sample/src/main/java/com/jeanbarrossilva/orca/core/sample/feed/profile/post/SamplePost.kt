package com.jeanbarrossilva.orca.core.sample.feed.profile.post

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.post.stat.Stat
import com.jeanbarrossilva.orca.core.feed.profile.post.stat.toggleable.ToggleableStat
import java.net.URL
import java.time.ZonedDateTime

/** [Post] whose operations are performed in memory and serves as a sample. */
internal data class SamplePost(
  override val id: String,
  override val author: Author,
  override val content: Content,
  override val publicationDateTime: ZonedDateTime,
  override val url: URL
) : Post() {
  override val comment = Stat<Post>()
  override val favorite = ToggleableStat<Profile>()
  override val repost = ToggleableStat<Profile>()
}
