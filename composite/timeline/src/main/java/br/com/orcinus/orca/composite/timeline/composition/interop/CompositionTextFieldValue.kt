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

import androidx.compose.runtime.Immutable
import br.com.orcinus.orca.composite.timeline.composition.CompositionTextField
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.style.Style
import br.com.orcinus.orca.std.markdown.style.contains
import br.com.orcinus.orca.std.markdown.style.`if`
import br.com.orcinus.orca.std.markdown.style.merge
import java.util.Objects

/**
 * Text editing state of a [CompositionTextField].
 *
 * @property text [Markdown] to be rendered.
 * @property selection Portion of the [text] that is selected.
 */
@Immutable
class CompositionTextFieldValue(
  val text: Markdown,
  val selection: IntRange = text.length..text.length
) {
  /** [selection], but with an exclusive last index. */
  @PublishedApi internal val exclusiveSelection = selection.first until selection.last

  override fun equals(other: Any?): Boolean {
    return other is CompositionTextFieldValue && text == other.text && selection == other.selection
  }

  override fun hashCode(): Int {
    return Objects.hash(text, selection)
  }

  override fun toString(): String {
    return "CompositionTextFieldValue(text=$text, selection=$selection)"
  }

  /**
   * Whether a portion of the [text] containing the specified [Style] is selected.
   *
   * @param T [Style] whose presence in the selection will be verified.
   */
  inline fun <reified T : Style> isSelected(): Boolean {
    return text.styles.filterIsInstance<T>().any { it.indices in exclusiveSelection }
  }

  /**
   * Toggles the specified [Style] in the selected portion of the [text].
   *
   * @param T [Style] to be either added or removed to the selection.
   * @param style Creates the [Style] to be added in case in isn't currently applied.
   */
  inline fun <reified T : Style> toggle(
    style: (indices: IntRange) -> T
  ): CompositionTextFieldValue {
    val styles =
      (text.styles % exclusiveSelection)
        .`if`({ none { it is T && it.indices in exclusiveSelection } }) {
          plus(style(exclusiveSelection))
        }
        .merge()
    val text = Markdown.styled("$text", styles)
    return CompositionTextFieldValue(text, selection)
  }

  companion object {
    /**
     * An empty-texted and unselected value.
     *
     * @see text
     * @see selection
     */
    val Empty = CompositionTextFieldValue(Markdown.empty, selection = IntRange.EMPTY)
  }
}

/**
 * Returns this value in case it isn't `null`; otherwise, the empty one.
 *
 * @see CompositionTextFieldValue.Companion.Empty
 */
internal fun CompositionTextFieldValue?.orEmpty(): CompositionTextFieldValue {
  return this ?: CompositionTextFieldValue.Empty
}
