/*
 * Copyright © 2024–2025 Orcinus
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

package br.com.orcinus.orca.composite.timeline.composition.interop

import android.content.Context
import android.text.Spanned
import br.com.orcinus.orca.composite.timeline.composition.interop.spanned.toMarkdown
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.style.Style

/**
 * Converts this [CharSequence] into [Markdown].
 *
 * @param context [Context] with which [Style]s are converted into spans.
 */
internal fun CharSequence.toMarkdown(context: Context): Markdown {
  return when (this) {
    is Markdown -> this
    is Spanned -> toMarkdown(context)
    else -> Markdown.unstyled("$this")
  }
}
