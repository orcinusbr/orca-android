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

package br.com.orcinus.orca.core.feed.profile.post.repost

import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.post.DeletablePost
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.feed.profile.post.stat.Stat
import br.com.orcinus.orca.core.feed.profile.post.stat.addable.AddableStat
import br.com.orcinus.orca.core.feed.profile.post.stat.toggleable.ToggleableStat
import java.net.URL
import java.time.ZonedDateTime

/**
 * [Post] that has been reposted by someone else.
 *
 * @param original [Post] from which the [Repost] derives.
 * @param reblogger [Author] by which the [Post] has been reblogged.
 */
fun Repost(original: Post, reblogger: Author): Repost {
  return Repost(
    original.id,
    original.author,
    reblogger,
    original.content,
    original.publicationDateTime,
    original.comment,
    original.favorite,
    original.repost,
    original.url
  ) {
    original.asDeletable()
  }
}

/**
 * [Post] that has been reposted by someone else.
 *
 * @param id Unique identifier.
 * @param author [Author] that has authored the [Post].
 * @param reblogger [Author] by which the [Post] has been reblogged.
 * @param content [Content] that's been composed by the [author].
 * @param publicationDateTime Zoned moment in time in which the [Post] was published.
 * @param comment [Stat] for the [Post]'s comments.
 * @param favorite [Stat] for the [Post]'s favorites.
 * @param reblog [Stat] for the [Post]'s reblogs.
 * @param url [URL] that leads to the [Post].
 * @param asDeletable Creates a [DeletablePost] from this [Repost].
 */
fun Repost(
  id: String,
  author: Author,
  reblogger: Author,
  content: Content,
  publicationDateTime: ZonedDateTime,
  comment: AddableStat<Post>,
  favorite: ToggleableStat<Profile>,
  reblog: ToggleableStat<Profile>,
  url: URL,
  asDeletable: (Repost) -> DeletablePost
): Repost {
  return object : Repost() {
    override val id = id
    override val author = author
    override val reposter = reblogger
    override val content = content
    override val publicationDateTime = publicationDateTime
    override val comment = comment
    override val favorite = favorite
    override val repost = reblog
    override val url = url

    override fun asDeletable(): DeletablePost {
      return asDeletable(this)
    }
  }
}
