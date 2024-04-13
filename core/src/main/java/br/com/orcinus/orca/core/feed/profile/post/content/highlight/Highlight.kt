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

package br.com.orcinus.orca.core.feed.profile.post.content.highlight

import br.com.orcinus.orca.core.InternalCoreApi
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import java.net.URL

/**
 * Main portion of a [Content] that redirects the user to another site.
 *
 * @param headline [Headline] with the main information.
 * @param url [URL] that leads to the external site.
 */
data class Highlight @InternalCoreApi constructor(val headline: Headline, val url: URL) {
  companion object
}
