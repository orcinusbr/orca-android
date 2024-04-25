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

package br.com.orcinus.orca.std.markdown.style

import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.uri.URIBuilder
import java.net.URI

/**
 * Sample [URI] to create mentions with.
 *
 * @see Markdown.Builder.mention
 * @see Style.Mention
 */
internal val Style.Mention.Companion.uri
  get() = URIBuilder.scheme("https").host("mastodon.social").path("@jeanbarrossilva").build()

/**
 * Sample username to mention.
 *
 * @see Markdown.Builder.mention
 * @see Style.Mention
 */
internal val Style.Mention.Companion.username
  get() = "jeanbarrossilva"
