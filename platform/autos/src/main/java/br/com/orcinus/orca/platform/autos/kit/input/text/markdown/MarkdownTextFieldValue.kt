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

package br.com.orcinus.orca.platform.autos.kit.input.text.markdown

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.style.Style
import java.util.Objects

/**
 * Text editing state of a [MarkdownTextField].
 *
 * @property text [Markdown] to be rendered.
 * @property selection Portion of the [text] that is selected.
 */
@Immutable
class MarkdownTextFieldValue
internal constructor(val text: Markdown, val selection: IntRange = IntRange.EMPTY) {
  override fun equals(other: Any?): Boolean {
    return other is MarkdownTextFieldValue && text == other.text && selection == other.selection
  }

  override fun hashCode(): Int {
    return Objects.hash(text, selection)
  }

  override fun toString(): String {
    return "MarkdownTextFieldValue(text=$text, selection=$selection)"
  }

  /**
   * Whether a portion of the [text] containing the specified [Style] is selected.
   *
   * @param T [Style] whose presence in the selection will be verified.
   */
  inline fun <reified T : Style> isSelected(): Boolean {
    return text.styles.filterIsInstance<T>().any { selection.any(it.indices::contains) }
  }

  companion object {
    /**
     * An empty-texted and unselected [MarkdownTextFieldValue].
     *
     * @see text
     * @see selection
     */
    val Empty = MarkdownTextFieldValue(Markdown.empty)
  }
}

/**
 * Remembers a [MarkdownTextFieldValue].
 *
 * @param text [Markdown] to be rendered.
 * @param selection Portion of the [text] that is selected.
 */
@Composable
fun rememberMarkdownTextFieldValue(
  text: Markdown,
  selection: IntRange = IntRange.EMPTY
): MarkdownTextFieldValue {
  return remember(text, selection) { MarkdownTextFieldValue(text, selection) }
}
