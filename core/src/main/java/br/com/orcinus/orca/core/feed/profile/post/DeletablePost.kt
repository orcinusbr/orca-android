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

/**
 * [Post] that can be deleted.
 *
 * @param delegate [Post] to which this [DeletablePost]'s functionality (apart from its deletion)
 *   will be delegated.
 * @see delete
 */
abstract class DeletablePost @InternalCoreApi constructor(private val delegate: Post) : Post {
  override val id = delegate.id
  override val author = delegate.author
  override val content = delegate.content
  override val publicationDateTime = delegate.publicationDateTime
  override val comment = delegate.comment
  override val favorite = delegate.favorite
  override val repost = delegate.repost
  override val url = delegate.url

  final override fun asDeletable(): DeletablePost {
    return this
  }

  /** Deletes this [DeletablePost]. */
  abstract suspend fun delete()

  companion object
}
