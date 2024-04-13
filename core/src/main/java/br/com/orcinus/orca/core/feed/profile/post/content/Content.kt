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
import br.com.orcinus.orca.std.styledstring.StyledString
import br.com.orcinus.orca.std.styledstring.style.type.Link
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
  val text: StyledString,
  val attachments: List<Attachment>,
  val highlight: Highlight? = null
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
    /**
     * Creates [Content] from the given [text].
     *
     * @param domain [Domain] of the [Instance] from which the [Content] is being created.
     * @param text [String] from which [Content] will be created.
     * @param attachments [Attachment]s containing the attached media.
     * @param headlineProvider [HeadlineProvider] that provides the [Headline].
     */
    @InternalCoreApi
    fun from(
      domain: Domain,
      text: StyledString,
      attachments: List<Attachment> = emptyList(),
      headlineProvider: HeadlineProvider
    ): Content {
      val links = text.styles.filterIsInstance<Link>()
      val externalLinks = links.filterNot { it.url.isOfResourceFrom(domain) }
      val highlightLink = externalLinks.firstOrNull()
      val highlightUrl = highlightLink?.url
      val headline = highlightUrl?.let { headlineProvider.provide(it) }
      val highlight = headline?.let { Highlight(it, highlightUrl) }
      val isHighlightLinkRemovable = text.isHighlightLinkRemovable(externalLinks, highlightLink)

      @Suppress("LocalVariableName")
      val _text = if (isHighlightLinkRemovable) text.withoutHighlightLink(highlightLink) else text

      return Content(_text, attachments, highlight)
    }
  }
}
