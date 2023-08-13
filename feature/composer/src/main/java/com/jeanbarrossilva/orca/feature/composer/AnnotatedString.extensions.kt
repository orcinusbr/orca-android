package com.jeanbarrossilva.orca.feature.composer

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import com.jeanbarrossilva.orca.core.sample.feed.profile.type.editable.replacingBy

/**
 * Gets the [SpanStyle] annotations applied within the specified [range].
 *
 * @param range [IntRange] representing the index from (inclusively) and to which (exclusively)
 * where the [SpanStyle] annotations to be obtained are.
 **/
internal fun AnnotatedString.getSpanStylesWithin(range: IntRange):
    List<AnnotatedString.Range<SpanStyle>> {
    return spanStyles.filter { annotation ->
        (annotation.start..annotation.end).any { index ->
            index in range
        }
    }
}

/**
 * Creates an [AnnotatedString] with this one's [spanStyle][AnnotatedString.spanStyles]s within the
 * [range] replaced by the result of [replacement]. If no [SpanStyle] is found, then an empty one
 * created and passed into [replacement] as if it was a preexisting one.
 *
 * @param range [IntRange] representing the index from (inclusively) and to which (exclusively)
 * where the [SpanStyle]s to be replaced are.
 * @param replacement Returns the [SpanStyle] to replace the current given one that's within the
 * [range].
 **/
internal fun AnnotatedString.replacingSpanStylesWithin(
    range: IntRange,
    replacement: SpanStyle.() -> SpanStyle
): AnnotatedString {
    val replacedSpanStyles = spanStyles
        .replacingBy({ copy(item = item.replacement()) }) { it in getSpanStylesWithin(range) }
        .ifEmpty {
            listOf(AnnotatedString.Range(SpanStyle().replacement(), range.first, range.last))
        }
    return AnnotatedString(text, replacedSpanStyles, paragraphStyles)
}
