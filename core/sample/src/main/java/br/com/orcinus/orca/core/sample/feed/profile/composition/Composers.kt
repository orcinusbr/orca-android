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

package br.com.orcinus.orca.core.sample.feed.profile.composition

import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.feed.profile.post.repost.Repost
import java.util.Objects

/** Utility class for [Composer]-based operations. */
internal object Composers {
  /** All [Post]s that have been composed and published by this [Composer]. */
  val Composer.posts
    @JvmStatic get() = postsFlow.value

  /**
   * Prepares the publishing of a sample [Post].
   *
   * @param content [Content] that has been composed.
   */
  @JvmStatic
  fun Composer.compose(content: Content): Composition {
    return Composition(this, content)
  }

  /** Converts this [Composer] into an [Author]. */
  @JvmStatic
  fun Composer.toAuthor(): Author {
    return Author(id, avatarLoader, name, account, profileURI = uri)
  }

  /**
   * Compares both [Composer]s structurally based on the values assigned to their common fields.
   *
   * @param T [Composer] to be compared.
   * @param left [Composer] to which the [right] one is compared.
   * @param right [Composer] to which the [left] one is compared.
   */
  @JvmStatic
  fun <T : Composer> equals(left: T, right: T): Boolean {
    return left.id == right.id &&
      left.account == right.account &&
      left.avatarLoader == right.avatarLoader &&
      left.name == right.name &&
      left.bio == right.bio &&
      left.followerCount == right.followerCount &&
      left.followingCount == right.followingCount &&
      left.uri == right.uri &&
      left.posts == right.posts
  }

  /**
   * Obtains the [Post] which is considered to be the main sample one.
   *
   * @throws NoSuchElementException If a [Post] that does not satisfy such criteria has not been
   *   published yet.
   */
  @JvmStatic
  @Throws(NoSuchElementException::class)
  fun Composer.getMainPostOrThrow(): Post {
    return posts.filterNot { it is Repost }.first()
  }

  /**
   * Computes a hash code for the given [composer].
   *
   * @param composer [Composer] whose values assigned to common fields are to be included in the
   *   hashing.
   * @param values Additional objects to be hashed.
   */
  @JvmStatic
  fun hash(composer: Composer, vararg values: Any?): Int {
    return Objects.hash(
      composer.id,
      composer.account,
      composer.avatarLoader,
      composer.name,
      composer.bio,
      composer.followerCount,
      composer.followingCount,
      composer.uri,
      composer.posts,
      *values
    )
  }
}
