/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

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
