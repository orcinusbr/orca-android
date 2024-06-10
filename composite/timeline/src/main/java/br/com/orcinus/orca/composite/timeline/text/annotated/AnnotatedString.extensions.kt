/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.composite.timeline.text.annotated

import androidx.compose.ui.text.AnnotatedString
import br.com.orcinus.orca.composite.timeline.text.annotated.span.StyleExtractor
import br.com.orcinus.orca.std.markdown.Markdown

/** Converts this [AnnotatedString] into [Markdown]. */
fun AnnotatedString.toMarkdown(): Markdown {
  val styles = spanStyles.flatMap { StyleExtractor.extractAll(it.item, it.start..it.end.dec()) }
  return Markdown.styled(text, styles)
}
