package com.jeanbarrossilva.orca.core.feed.profile.post.test

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.post.stat.Stat
import com.jeanbarrossilva.orca.core.feed.profile.post.stat.toggleable.ToggleableStat
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.sample
import java.net.URL
import java.time.ZonedDateTime

/** Local [Post] that defaults its properties' values to [Post.Companion.sample]'s. */
internal class TestPost(
  override val id: String = Post.sample.id,
  override val author: Author = Post.sample.author,
  override val content: Content = Post.sample.content,
  override val publicationDateTime: ZonedDateTime = Post.sample.publicationDateTime,
  override val comment: Stat<Post> = Post.sample.comment,
  override val favorite: ToggleableStat<Profile> = Post.sample.favorite,
  override val repost: ToggleableStat<Profile> = Post.sample.repost,
  override val url: URL = Post.sample.url
) : Post()
