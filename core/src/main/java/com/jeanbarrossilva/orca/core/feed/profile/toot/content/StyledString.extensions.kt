package com.jeanbarrossilva.orca.core.feed.profile.toot.content

import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import com.jeanbarrossilva.orca.std.styledstring.style.type.Link
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Returns whether the [Highlight] link can be removed from this [StyledString].
 *
 * @param externalLinks [Link]s that link to resources outside of a given [Domain].
 * @param highlightLink [Link] considered to be the highlight from the external ones.
 */
@OptIn(ExperimentalContracts::class)
internal fun StyledString.isHighlightLinkRemovable(
  externalLinks: List<Link>,
  highlightLink: Link?
): Boolean {
  contract { returns() implies (highlightLink != null) }
  return externalLinks.size == 1 &&
    highlightLink != null &&
    trimEnd().endsWith(substring(highlightLink.indices))
}

/**
 * Returns this [StyledString] minus the [Highlight] link.
 *
 * @param highlightLink [Link] considered to be the highlight from the external ones.
 */
internal fun StyledString.withoutHighlightLink(highlightLink: Link): StyledString {
  return copy { removeRange(highlightLink.indices).trim() }
}
