package com.jeanbarrossilva.orca.core.feed.profile.toot.content

import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Headline
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.HeadlineProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.core.instance.domain.isOfResourceFrom
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import com.jeanbarrossilva.orca.std.styledstring.style.type.Link
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
    return other is Content && text == other.text && highlight == other.highlight
  }

  override fun hashCode(): Int {
    return Objects.hash(text, highlight)
  }

  override fun toString(): String {
    return "Content(text=$text, highlight=$highlight)"
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
