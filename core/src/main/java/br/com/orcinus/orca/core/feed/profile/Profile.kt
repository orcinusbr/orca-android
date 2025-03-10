/*
 * Copyright © 2023–2025 Orcinus
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

package br.com.orcinus.orca.core.feed.profile

import br.com.orcinus.orca.core.InternalCoreApi
import br.com.orcinus.orca.core.feed.Pages
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoader
import br.com.orcinus.orca.std.markdown.Markdown
import java.io.Serializable
import java.net.URI
import kotlinx.coroutines.flow.Flow

/** A user's profile. */
interface Profile : Serializable {
  /** Unique identifier. */
  val id: String

  /** Unique identifier within an instance. */
  val account: Account

  /** [ImageLoader] that loads the avatar. */
  val avatarLoader: SomeImageLoader

  /** Name to be displayed. */
  val name: String

  /** Describes who the owner is and/or provides information regarding this [Profile]. */
  val bio: Markdown

  /** Amount of followers. */
  val followerCount: Int

  /** Amount of following. */
  val followingCount: Int

  /**
   * [URI] that leads to the webpage of the instance through which this [Profile] can be accessed.
   */
  val uri: URI

  /**
   * Callback called whenever the [Post]s published by the owner of this [Profile] are requested to
   * be obtained.
   *
   * @param page Valid page in which the [Post]s to be obtained are.
   */
  @InternalCoreApi suspend fun onPostsObtainance(page: Int): Flow<List<Post>>

  companion object
}

/**
 * Obtains the [Post]s that have been published by the owner of this [Profile].
 *
 * @param page Page in which the [Post]s to be obtained are.
 */
suspend fun Profile.getPosts(page: Int) = Pages.validate(page).map { onPostsObtainance(it) }
