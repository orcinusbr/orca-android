package com.jeanbarrossilva.orca.core.instance

import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.Authenticator
import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.ProfileProvider
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.PostProvider
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearcher
import com.jeanbarrossilva.orca.core.instance.domain.Domain

/** An [Instance] with a generic [Authenticator]. */
typealias SomeInstance = Instance<*>

/**
 * Site at which the user is, from which all operations can be performed.
 *
 * @param T [Authenticator] to authenticate the user with.
 */
abstract class Instance<T : Authenticator> {
  /** Unique identifier of the server. */
  abstract val domain: Domain

  /** [Instance]-specific [Authenticator] through which authentication can be done. */
  abstract val authenticator: T

  /**
   * [Instance]-specific [AuthenticationLock] by which features can be locked or unlocked by an
   * authentication "wall".
   */
  abstract val authenticationLock: AuthenticationLock<T>

  /** [Instance]-specific [FeedProvider] that provides the [Post]s in the timeline. */
  abstract val feedProvider: FeedProvider

  /** [Instance]-specific [ProfileProvider] for providing [Profile]s. */
  abstract val profileProvider: ProfileProvider

  /** [Instance]-specific [ProfileSearcher] by which search for [Profile]s can be made. */
  abstract val profileSearcher: ProfileSearcher

  /** [Instance]-specific [PostProvider] that provides [Post]s. */
  abstract val postProvider: PostProvider

  companion object
}
