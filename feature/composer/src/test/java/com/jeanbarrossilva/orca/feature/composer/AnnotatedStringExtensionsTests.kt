package com.jeanbarrossilva.orca.feature.composer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import org.junit.Assert.assertEquals
import org.junit.Test

internal class AnnotatedStringExtensionsTests {
    @Test
    fun `GIVEN an AnnotatedString with an un-styled portion WHEN getting its span styles THEN it's an empty list`() { // ktlint-disable max-line-length
        assertEquals(
            emptyList<AnnotatedString.Range<SpanStyle>>(),
            buildAnnotatedString {
                append("Hello, ")
                withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                    append("world")
                }
            }
                .getSpanStylesWithin(0..5)
        )
    }

    @Test
    fun `GIVEN an AnnotatedString with a portion of it styled WHEN getting its span styles THEN it's equivalent to its styling`() { // ktlint-disable max-line-length
        assertEquals(
            listOf(
                AnnotatedString.Range(SpanStyle(fontWeight = FontWeight.Bold), start = 0, end = 5),
                AnnotatedString.Range(SpanStyle(Color.Blue), start = 12, end = 13)
            ),
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Hello") }
                append(", world")
                withStyle(SpanStyle(Color.Blue)) { append('!') }
            }
                .getSpanStylesWithin(0..13)
        )
    }

    @Test
    fun `GIVEN an AnnotatedString without span styles WHEN replacing the ones within a range THEN the replacement is added`() { // ktlint-disable max-line-length
        assertEquals(
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Hello") }
                append(", world!")
            },
            AnnotatedString("Hello, world!")
                .replacingSpanStylesWithin(0..5) { copy(fontWeight = FontWeight.Bold) }
        )
    }
}
