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

package com.jeanbarrossilva.orca.core.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import java.io.Serializable
import java.net.URL
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
  val bio: StyledString

  /** Amount of followers. */
  val followerCount: Int

  /** Amount of following. */
  val followingCount: Int

  /**
   * [URL] that leads to the webpage of the instance through which this [Profile] can be accessed.
   */
  val url: URL

  /**
   * Gets the [Post]s that have been published by the owner of this [Profile].
   *
   * @param page Page in which the [Post]s to be obtained are.
   */
  suspend fun getPosts(page: Int): Flow<List<Post>>

  companion object
}
