/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
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
