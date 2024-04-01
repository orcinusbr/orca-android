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

package com.jeanbarrossilva.orca.core.feed.profile.post.content

import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Highlight
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
