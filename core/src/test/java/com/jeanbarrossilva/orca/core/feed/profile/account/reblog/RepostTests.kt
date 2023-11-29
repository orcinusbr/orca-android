package com.jeanbarrossilva.orca.core.feed.profile.account.reblog

import com.jeanbarrossilva.orca.core.feed.profile.post.repost.Repost
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.repost.sample
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RepostTests {
  @Test
  fun createsRepost() {
    assertEquals(
      object : Repost() {
        override val id = Repost.sample.id
        override val author = Repost.sample.author
        override val reposter = Repost.sample.reposter
        override val content = Repost.sample.content
        override val publicationDateTime = Repost.sample.publicationDateTime
        override val comment = Repost.sample.comment
        override val favorite = Repost.sample.favorite
        override val repost = Repost.sample.repost
        override val url = Repost.sample.url
      },
      Repost(
        Repost.sample.id,
        Repost.sample.author,
        Repost.sample.reposter,
        Repost.sample.content,
        Repost.sample.publicationDateTime,
        Repost.sample.comment,
        Repost.sample.favorite,
        Repost.sample.repost,
        Repost.sample.url
      )
    )
  }

  @Test
  fun createsRepostFromOriginalPost() {
    assertEquals(Repost.sample, Repost(Repost.sample, Repost.sample.reposter))
  }
}
