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

package br.com.orcinus.orca.core.feed

import br.com.orcinus.orca.core.InternalCoreApi
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.TermMuter
import kotlinx.coroutines.flow.Flow

/**
 * Provides a user's feed (the [Post]s shown to them based on who they follow) through
 * [onProvision].
 */
abstract class FeedProvider @InternalCoreApi constructor() {
  /** [TermMuter] by which [Post]s with muted terms will be filtered out. */
  protected abstract val termMuter: TermMuter

  /**
   * Provides the feed of the current [Actor].
   *
   * @param page Page at which the emitted [Post]s are in the feed.
   */
  suspend fun provide(page: Int) =
    Pages.validate(page).map { validPage ->
      onProvision(validPage).filterEach { post -> !termMuter.isMuted(post.content) }
    }

  /**
   * Callback called whenever the feed of the current [Actor] is requested to be provided.
   *
   * @param page Valid page at which the emitted [Post]s are in the feed.
   */
  protected abstract suspend fun onProvision(page: Int): Flow<List<Post>>
}
