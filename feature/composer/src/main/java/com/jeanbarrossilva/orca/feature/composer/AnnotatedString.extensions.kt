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

package com.jeanbarrossilva.orca.feature.composer

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import com.jeanbarrossilva.orca.core.sample.feed.profile.type.editable.replacingBy

/**
 * Gets the [SpanStyle] annotations applied within the specified [range].
 *
 * @param range [IntRange] representing the index from (inclusively) and to which (exclusively)
 *   where the [SpanStyle] annotations to be obtained are.
 */
internal fun AnnotatedString.getSpanStylesWithin(
  range: IntRange
): List<AnnotatedString.Range<SpanStyle>> {
  return spanStyles.filter { annotation ->
    (annotation.start..annotation.end).any { index -> index in range }
  }
}

/**
 * Creates an [AnnotatedString] with this one's [spanStyle][AnnotatedString.spanStyles]s within the
 * [range] replaced by the result of [replacement]. If no [SpanStyle] is found, then an empty one
 * created and passed into [replacement] as if it was a preexisting one.
 *
 * @param range [IntRange] representing the index from (inclusively) and to which (exclusively)
 *   where the [SpanStyle]s to be replaced are.
 * @param replacement Returns the [SpanStyle] to replace the current given one that's within the
 *   [range].
 */
internal fun AnnotatedString.replacingSpanStylesWithin(
  range: IntRange,
  replacement: SpanStyle.() -> SpanStyle
): AnnotatedString {
  val replacedSpanStyles =
    spanStyles
      .replacingBy({ copy(item = item.replacement()) }) { it in getSpanStylesWithin(range) }
      .ifEmpty { listOf(AnnotatedString.Range(SpanStyle().replacement(), range.first, range.last)) }
  return AnnotatedString(text, replacedSpanStyles, paragraphStyles)
}
