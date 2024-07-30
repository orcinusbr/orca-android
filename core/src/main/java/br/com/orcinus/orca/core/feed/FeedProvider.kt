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

package br.com.orcinus.orca.core.feed

import br.com.orcinus.orca.core.InternalCoreApi
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.TermMuter
import kotlinx.coroutines.flow.Flow

/**
 * Provides a user's feed (the [Post]s shown to them based on who they follow) through [onProvide].
 */
abstract class FeedProvider @InternalCoreApi constructor() {
  /** [TermMuter] by which [Post]s with muted terms will be filtered out. */
  protected abstract val termMuter: TermMuter

  /**
   * [IllegalArgumentException] thrown when a user that doesn't exist is requested to be provided.
   */
  class NonexistentUserException @InternalCoreApi constructor(override val cause: Throwable?) :
    IllegalArgumentException("Feed cannot be loaded because the user doesn't exist.")

  /**
   * Provides the feed of the user identified as [userID].
   *
   * @param userID ID of the user whose feed will be provided.
   * @param page Index of the [Post]s that compose the feed.
   * @throws NonexistentUserException If no user with such [userID] exists.
   * @throws IndexOutOfBoundsException If the page is invalid.
   * @see ensurePageValidity
   */
  suspend fun provide(userID: String, page: Int): Flow<List<Post>> {
    ensureContainsUser(userID)
    ensurePageValidity(page)
    return onProvide(userID, page).filterEach { !termMuter.isMuted(it.content) }
  }

  /**
   * Whether a user identified as [userID] exists.
   *
   * @param userID ID of the user whose existence will be checked.
   */
  protected abstract suspend fun containsUser(userID: String): Boolean

  /** Creates a variant-specific [NonexistentUserException]. */
  protected abstract fun createNonexistentUserException(): NonexistentUserException

  /**
   * Provides the feed of the user identified as [userID].
   *
   * @param userID ID of the user whose feed will be provided.
   * @param page Index of the [Post]s that compose the feed.
   */
  protected abstract suspend fun onProvide(userID: String, page: Int): Flow<List<Post>>

  /**
   * Ensures that a user identified as [userID] exists.
   *
   * @param userID ID of the user whose existence will be ensured.
   * @throws NonexistentUserException If no user with such [userID] exists.
   */
  private suspend fun ensureContainsUser(userID: String) {
    if (!containsUser(userID)) {
      throw createNonexistentUserException()
    }
  }

  /**
   * Ensures that the [page] is valid; that is, if it is a positive [Int].
   *
   * @param page Page whose validity will be ensured.
   * @throws IndexOutOfBoundsException If the page is invalid.
   */
  private fun ensurePageValidity(page: Int) {
    if (page < 0) {
      throw IndexOutOfBoundsException("Page out of range: $page.")
    }
  }
}
