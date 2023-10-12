package com.jeanbarrossilva.orca.core.feed.profile.toot.reblog

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.toot.stat.Stat
import com.jeanbarrossilva.orca.core.feed.profile.toot.stat.toggleable.ToggleableStat
import java.net.URL
import java.time.ZonedDateTime

/**
 * [Toot] that has been reblogged by someone else.
 *
 * @param original [Toot] from which the [Reblog] derives.
 * @param reblogger [Author] by which the [Toot] has been reblogged.
 */
fun Reblog(original: Toot, reblogger: Author): Reblog {
  return Reblog(
    original.id,
    original.author,
    reblogger,
    original.content,
    original.publicationDateTime,
    original.comment,
    original.favorite,
    original.reblog,
    original.url
  )
}

/**
 * [Toot] that has been reblogged by someone else.
 *
 * @param id Unique identifier.
 * @param author [Author] that has authored the [Toot].
 * @param reblogger [Author] by which the [Toot] has been reblogged.
 * @param content [Content] that's been composed by the [author].
 * @param publicationDateTime Zoned moment in time in which the [Toot] was published.
 * @param comment [Stat] for the [Toot]'s comments.
 * @param favorite [Stat] for the [Toot]'s favorites.
 * @param reblog [Stat] for the [Toot]'s reblogs.
 * @param url [URL] that leads to the [Toot].
 */
fun Reblog(
  id: String,
  author: Author,
  reblogger: Author,
  content: Content,
  publicationDateTime: ZonedDateTime,
  comment: Stat<Toot>,
  favorite: ToggleableStat<Profile>,
  reblog: ToggleableStat<Profile>,
  url: URL
): Reblog {
  return object : Reblog() {
    override val id = id
    override val author = author
    override val reblogger = reblogger
    override val content = content
    override val publicationDateTime = publicationDateTime
    override val comment = comment
    override val favorite = favorite
    override val reblog = reblog
    override val url = url
  }
}
