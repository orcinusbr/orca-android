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
