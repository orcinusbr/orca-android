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

package br.com.orcinus.orca.feature.composer

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration

/** Whether the selected portion of the [text][TextFieldValue.text] is bold. */
internal val TextFieldValue.isSelectionBold
  get() =
    annotatedString.getSpanStylesWithin(selection.start..selection.end).any {
      it.item.fontWeight == FontWeight.Bold
    }

/** Whether the selected portion of the [text][TextFieldValue.text] is italicized. */
internal val TextFieldValue.isSelectionItalicized
  get() =
    annotatedString.getSpanStylesWithin(selection.start..selection.end).any {
      it.item.fontStyle == FontStyle.Italic
    }

/** Whether the selected portion of the [text][TextFieldValue.text] is underlined. */
internal val TextFieldValue.isSelectionUnderlined
  get() =
    annotatedString.getSpanStylesWithin(selection.start..selection.end).any {
      it.item.textDecoration == TextDecoration.Underline
    }

/**
 * Creates a [TextFieldValue] that's identical to this one, except for having the selected portion
 * of its [text][TextFieldValue.text] styled as bold if [isBold] is `true` or normally if it isn't.
 */
internal fun TextFieldValue.withBoldSelection(isBold: Boolean): TextFieldValue {
  return replacingSelectedAnnotatedStringSpanStyles {
    copy(fontWeight = if (isBold) FontWeight.Bold else null)
  }
}

/**
 * Creates a [TextFieldValue] that's identical to this one, except for having the selection portion
 * of its [text][TextFieldValue.text] styled as italic if [isItalic] is `true` or normally if it
 * isn't.
 */
internal fun TextFieldValue.withItalicizedSelection(isItalic: Boolean): TextFieldValue {
  return replacingSelectedAnnotatedStringSpanStyles {
    copy(fontStyle = if (isItalic) FontStyle.Italic else null)
  }
}

/**
 * Creates a [TextFieldValue] that's identical to this one, except for having the selection portion
 * of its [text][TextFieldValue.text] decorated with an underline if [isUnderlined] is `true` or
 * undecorated if it isn't.
 */
internal fun TextFieldValue.withUnderlinedSelection(isUnderlined: Boolean): TextFieldValue {
  return replacingSelectedAnnotatedStringSpanStyles {
    copy(textDecoration = if (isUnderlined) TextDecoration.Underline else null)
  }
}

/**
 * Replaces the [annotatedString][TextFieldValue.annotatedString]'s [SpanStyle]s that style the
 * selected portion of the [text][TextFieldValue.text] by the result of [replacement].
 *
 * @param replacement Returns the [SpanStyle] to replace the current given one that's been applied
 *   to the selected [text][TextFieldValue.text].
 */
private fun TextFieldValue.replacingSelectedAnnotatedStringSpanStyles(
  replacement: SpanStyle.() -> SpanStyle
): TextFieldValue {
  return copy(
    annotatedString =
      annotatedString.replacingSpanStylesWithin(selection.start..selection.end, replacement)
  )
}
