/*
 * Copyright © 2023–2024 Orcinus
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

package com.jeanbarrossilva.orca.core.instance

import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.Authenticator
import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.ProfileProvider
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.provider.PostProvider
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
