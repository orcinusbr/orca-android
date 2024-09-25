/*
 * Copyright © 2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

@file:JvmName("Composers")

package br.com.orcinus.orca.core.sample.feed.profile.composition

import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.sample.InternalSampleApi
import java.time.ZonedDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/** [Profile] that allows for a [Post] to be composed and published. */
interface Composer : Profile {
  /** [MutableStateFlow] to which every [Post] composed by this [Composer] is emitted. */
  val postsFlow: MutableStateFlow<List<Post>>
    @InternalSampleApi get

  override suspend fun getPosts(page: Int): Flow<List<Post>> {
    return postsFlow.map {
      it.windowed(MAX_POST_COUNT_PER_PAGE, partialWindows = true).getOrElse(page) { _ ->
        emptyList()
      }
    }
  }

  /**
   * Creates a [Post].
   *
   * @param author [Author] that has authored the [Post].
   * @param content [Content] that's been composed by the [Author].
   * @param publicationDateTime Zoned moment in time in which the [Post] was published.
   */
  @InternalSampleApi
  fun createPost(author: Author, content: Content, publicationDateTime: ZonedDateTime): Post

  companion object {
    /** Maximum amount of [Post]s emitted by the [Flow] that results from calling [getPosts]. */
    const val MAX_POST_COUNT_PER_PAGE = 50
  }
}
