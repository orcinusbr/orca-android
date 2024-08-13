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

package br.com.orcinus.orca.core.feed.profile.post

import br.com.orcinus.orca.core.InternalCoreApi
import br.com.orcinus.orca.core.auth.actor.Actor

/**
 * [Post] that belongs to the authenticated [Actor].
 *
 * @param delegate [Post] to which this [OwnedPost]'s characteristics will be delegated.
 */
abstract class OwnedPost @InternalCoreApi constructor(private val delegate: Post) : Post() {
  override val actorProvider = delegate.getActorProvider()
  override val id = delegate.id
  override val author = delegate.author
  override val content = delegate.content
  override val publicationDateTime = delegate.publicationDateTime
  override val comment = delegate.comment
  override val favorite = delegate.favorite
  override val repost = delegate.repost
  override val uri = delegate.uri

  final override suspend fun toOwnedPost(): OwnedPost {
    return this
  }

  /** Removes this [OwnedPost]. */
  abstract suspend fun remove()

  companion object
}
