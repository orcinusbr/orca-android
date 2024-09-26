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

package br.com.orcinus.orca.core.feed.profile.post.content

import br.com.orcinus.orca.core.InternalCoreApi
import br.com.orcinus.orca.core.feed.profile.post.content.highlight.Headline
import br.com.orcinus.orca.core.feed.profile.post.content.highlight.HeadlineProvider
import br.com.orcinus.orca.core.feed.profile.post.content.highlight.Highlight
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.instance.domain.isOfResourceFrom
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.style.Style
import java.net.URI
import java.util.Objects

/**
 * Part that's been composed by the user.
 *
 * @param text Written content.
 * @param attachments [Attachment]s containing the attached media.
 * @param highlight [Highlight] from the [text].
 */
class Content
private constructor(
  val text: Markdown,
  val attachments: List<Attachment>,
  val highlight: Highlight?
) {
  override fun equals(other: Any?): Boolean {
    return other is Content &&
      text == other.text &&
      attachments == other.attachments &&
      highlight == other.highlight
  }

  override fun hashCode(): Int {
    return Objects.hash(text, attachments, highlight)
  }

  override fun toString(): String {
    return "Content(text=$text, attachments=$attachments, highlight=$highlight)"
  }

  companion object {
    /** [Content] with an empty [text], no [attachments] and without a [highlight]. */
    @InternalCoreApi
    @JvmStatic
    val empty = Content(text = Markdown.empty, attachments = emptyList(), highlight = null)

    /**
     * Creates [Content], automatically defining a [Highlight] based on a URL which may be present
     * in the [text]. It might then be removed from the final [text], depending on whether it was a
     * trailing one and not related to the [domain].
     *
     * @param domain [Domain] of the [Instance] from which the [Content] is being created.
     * @param text [String] from which [Content] will be created.
     * @param attachments [Attachment]s containing the attached media.
     * @param headlineProvider [HeadlineProvider] that provides the [Headline].
     * @see URI.isOfResourceFrom
     */
    @InternalCoreApi
    @JvmStatic
    fun from(
      domain: Domain,
      text: Markdown,
      attachments: List<Attachment> = emptyList(),
      headlineProvider: HeadlineProvider
    ): Content {
      val isEmpty = text === Markdown.empty && attachments.isEmpty()
      return if (isEmpty) {
        empty
      } else {
        create(domain, text, attachments, headlineProvider)
      }
    }

    /**
     * Creates [Content] from the given [text].
     *
     * @param domain [Domain] of the [Instance] from which the [Content] is being created.
     * @param text [String] from which [Content] will be created.
     * @param attachments [Attachment]s containing the attached media.
     * @param headlineProvider [HeadlineProvider] that provides the [Headline].
     */
    @JvmStatic
    private fun create(
      domain: Domain,
      text: Markdown,
      attachments: List<Attachment>,
      headlineProvider: HeadlineProvider
    ): Content {
      return with(text) {
        styles
          .filterIsInstance<Style.Link>()
          .filterNot { it.uri.isOfResourceFrom(domain) }
          .singleOrNull()
          ?.takeIf { trimEnd().endsWith(substring(it.indices)) }
          ?.let { link -> headlineProvider.provide(link.uri)?.let { headline -> link to headline } }
          ?.let { (link, headline) -> link.indices to Highlight(headline, link.uri) }
          ?.let { (it as Pair<Any, Highlight>).copy(first = copy { removeRange(it.first).trim() }) }
          ?.let { (newText, highlight) -> Content(newText as Markdown, attachments, highlight) }
      }
        ?: Content(text, attachments, highlight = null)
    }
  }
}
