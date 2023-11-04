package com.jeanbarrossilva.orca.core.feed.profile.account.reblog

import com.jeanbarrossilva.orca.core.feed.profile.toot.reblog.Reblog
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.toot.reblog.sample
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ReblogTests {
  @Test
  fun createsReblog() {
    assertEquals(
      object : Reblog() {
        override val id = Reblog.sample.id
        override val author = Reblog.sample.author
        override val reblogger = Reblog.sample.reblogger
        override val content = Reblog.sample.content
        override val publicationDateTime = Reblog.sample.publicationDateTime
        override val comment = Reblog.sample.comment
        override val favorite = Reblog.sample.favorite
        override val reblog = Reblog.sample.reblog
        override val url = Reblog.sample.url
      },
      Reblog(
        Reblog.sample.id,
        Reblog.sample.author,
        Reblog.sample.reblogger,
        Reblog.sample.content,
        Reblog.sample.publicationDateTime,
        Reblog.sample.comment,
        Reblog.sample.favorite,
        Reblog.sample.reblog,
        Reblog.sample.url
      )
    )
  }

  @Test
  fun createsReblogFromOriginalToot() {
    assertEquals(Reblog.sample, Reblog(Reblog.sample, Reblog.sample.reblogger))
  }
}
