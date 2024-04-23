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

package br.com.orcinus.orca.std.markdown.style.type.test

import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.style.type.Mention
import java.net.URL

/**
 * Sample [URL] to create mentions with.
 *
 * @see Markdown.Builder.mention
 * @see Mention
 */
internal val Mention.Companion.url
  get() = URL("https://mastodon.social/@jeanbarrossilva")

/**
 * Sample username to mention.
 *
 * @see Markdown.Builder.mention
 * @see Mention
 */
internal val Mention.Companion.username
  get() = "jeanbarrossilva"
