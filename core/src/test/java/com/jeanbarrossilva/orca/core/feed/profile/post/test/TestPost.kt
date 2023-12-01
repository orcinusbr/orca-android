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
