/*
 * Copyright © 2024 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

@file:JvmName("SpanStyles")

package com.jeanbarrossilva.orca.composite.timeline.text.annotated.span

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isUnspecified
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.isUnspecified
import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import com.jeanbarrossilva.orca.std.styledstring.style.type.Bold
import com.jeanbarrossilva.orca.std.styledstring.style.type.Email
import com.jeanbarrossilva.orca.std.styledstring.style.type.Hashtag
import com.jeanbarrossilva.orca.std.styledstring.style.type.Italic
import com.jeanbarrossilva.orca.std.styledstring.style.type.Link
import com.jeanbarrossilva.orca.std.styledstring.style.type.Mention
import java.net.MalformedURLException
import java.net.URL

/**
 * Font feature setting that specifies the category of a [SpanStyle] that has been created for a
 * [Link].
 *
 * @see createLinkSpanStyle
 */
private const val SPAN_STYLE_LINK_CATEGORY_SETTING = "category: "

/**
 * Specification of [SPAN_STYLE_LINK_CATEGORY_SETTING] for denoting that the [SpanStyle] has been
 * created for an [Email].
 */
private const val SPAN_STYLE_LINK_CATEGORY_SETTING_EMAIL_SPEC = "email"

/**
 * Specification of [SPAN_STYLE_LINK_CATEGORY_SETTING] for denoting that the [SpanStyle] has been
 * created for a [Hashtag].
 */
private const val SPAN_STYLE_LINK_CATEGORY_SETTING_HASHTAG_SPEC = "hashtag"

/**
 * Specification of [SPAN_STYLE_LINK_CATEGORY_SETTING] for denoting that the [SpanStyle] has been
 * created for a [Mention].
 */
private const val SPAN_STYLE_LINK_CATEGORY_SETTING_MENTION_SPEC = "mention"

/**
 * Description of the text (mentioned [Account], [URL], ...) that is styled by this [SpanStyle].
 *
 * @throws IllegalStateException If no category is attached.
 * @see createLinkSpanStyle
 */
private val SpanStyle.category
  @Throws(IllegalStateException::class)
  get() =
    fontFeatureSettings
      ?.substringAfter(SPAN_STYLE_LINK_CATEGORY_SETTING, missingDelimiterValue = "")
      ?.substringBefore(" ")
      ?.ifEmpty { null }
      ?.trim()
      ?: error("No category has been attached to $fontFeatureSettings.")

/**
 * [SpanStyle] into which an emboldened portion within a [StyledString] will be turned when
 * converting it into an [AnnotatedString].
 *
 * @see Bold
 * @see StyledString.toAnnotatedString
 */
@JvmField internal val BoldSpanStyle = SpanStyle(fontWeight = FontWeight.Bold)

/**
 * [SpanStyle] into which an italicized portion within a [StyledString] will be turned when
 * converting it into an [AnnotatedString].
 *
 * @see Italic
 * @see StyledString.toAnnotatedString
 */
@JvmField internal val ItalicSpanStyle = SpanStyle(fontStyle = FontStyle.Italic)

/** Whether this [SpanStyle] has been created for an [Email]. */
internal val SpanStyle.isForEmail
  get() =
    fontFeatureSettings?.let {
      SPAN_STYLE_LINK_CATEGORY_SETTING + SPAN_STYLE_LINK_CATEGORY_SETTING_EMAIL_SPEC in it
    }
      ?: false

/** Whether this [SpanStyle] has been created for a [Hashtag]. */
internal val SpanStyle.isForHashtag
  get() =
    fontFeatureSettings?.let {
      SPAN_STYLE_LINK_CATEGORY_SETTING + SPAN_STYLE_LINK_CATEGORY_SETTING_HASHTAG_SPEC in it
    }
      ?: false

/** Whether this [SpanStyle] has been created for a [Link]. */
internal val SpanStyle.isForLink
  get() = fontFeatureSettings?.let { SPAN_STYLE_LINK_CATEGORY_SETTING in it } ?: false

/** Whether this [SpanStyle] has been created for a [Mention]. */
internal val SpanStyle.isForMention
  get() =
    fontFeatureSettings?.let {
      SPAN_STYLE_LINK_CATEGORY_SETTING + SPAN_STYLE_LINK_CATEGORY_SETTING_MENTION_SPEC in it
    }
      ?: false

/**
 * [URL] of the [Profile] that has been attached as the "mention" [category] of this [SpanStyle].
 *
 * @throws IllegalStateException If no [category] is attached.
 * @throws MalformedURLException If the [URL] or the call to "url" is either malformed or missing.
 * @see Profile.url
 */
internal val SpanStyle.mention
  @Throws(IllegalStateException::class, MalformedURLException::class)
  get() =
    category
      .substringAfter("$SPAN_STYLE_LINK_CATEGORY_SETTING_MENTION_SPEC ", missingDelimiterValue = "")
      .substringBefore(" ", missingDelimiterValue = "")
      .let(::URL)

/**
 * [URL] that has been attached as the [category] of this [SpanStyle].
 *
 * @throws IllegalStateException If no [category] is attached.
 * @throws MalformedURLException If the [URL] or the call to "url" is either malformed or missing.
 */
internal val SpanStyle.url
  @Throws(IllegalStateException::class, MalformedURLException::class)
  get() =
    category
      .substringAfter("url(", missingDelimiterValue = "")
      .substringBeforeLast(")", missingDelimiterValue = "")
      .ifEmpty {
        throw MalformedURLException("Metadata with missing or malformed call to \"url\".")
      }
      .let(::URL)

/**
 * Creates a [SpanStyle] into which an [Email] within a [StyledString] will be turned when
 * converting it into an [AnnotatedString].
 *
 * @param colors [Colors] by which the [SpanStyle] can be colored.
 * @see StyledString.toAnnotatedString
 * @see SpanStyle.category
 */
internal fun createEmailSpanStyle(colors: Colors): SpanStyle {
  return createLinkSpanStyle(colors, category = SPAN_STYLE_LINK_CATEGORY_SETTING_EMAIL_SPEC)
}

/**
 * Creates a [SpanStyle] into which a [Hashtag] within a [StyledString] will be turned when
 * converting it into an [AnnotatedString].
 *
 * @param colors [Colors] by which the [SpanStyle] can be colored.
 * @see StyledString.toAnnotatedString
 * @see SpanStyle.category
 */
internal fun createHashtagSpanStyle(colors: Colors): SpanStyle {
  return createLinkSpanStyle(colors, SPAN_STYLE_LINK_CATEGORY_SETTING_HASHTAG_SPEC)
}

/**
 * Creates a [SpanStyle] into which a [Mention] within a [StyledString] will be turned when
 * converting it into an [AnnotatedString].
 *
 * @param colors [Colors] by which the [SpanStyle] can be colored.
 * @param url [URL] that leads to the [Profile] that has been mentioned.
 * @see StyledString.toAnnotatedString
 * @see SpanStyle.category
 */
internal fun createMentionSpanStyle(colors: Colors, url: URL): SpanStyle {
  return createLinkSpanStyle(
    colors,
    category = "$SPAN_STYLE_LINK_CATEGORY_SETTING_MENTION_SPEC $url"
  )
}

/**
 * Creates a [SpanStyle] into which a [Link] within a [StyledString] will be turned when converting
 * it into an [AnnotatedString].
 *
 * @param colors [Colors] by which the [SpanStyle] can be colored.
 * @param category Describes the text being styled.
 * @see StyledString.toAnnotatedString
 * @see SpanStyle.category
 */
internal fun createLinkSpanStyle(colors: Colors, category: String? = null): SpanStyle {
  return SpanStyle(
    colors.link.asColor,
    fontFeatureSettings = category?.let { SPAN_STYLE_LINK_CATEGORY_SETTING + category }
  )
}

/**
 * Returns whether the given [SpanStyle] contains all of the attributes that this one has set as
 * being explicitly defined (that is, neither an "unspecified" instance of the object, such as
 * [Color.Unspecified], nor `null`).
 *
 * @param other [SpanStyle] to compare the receiver one with.
 */
internal operator fun SpanStyle.contains(other: SpanStyle): Boolean {
  return (other.background.isUnspecified || background == other.background) &&
    (other.baselineShift == null || baselineShift == other.baselineShift) &&
    (other.brush == null || brush == other.brush) &&
    (other.color.isUnspecified || color == other.color) &&
    (other.drawStyle == null || drawStyle == other.drawStyle) &&
    (other.fontFamily == null || fontFamily == other.fontFamily) &&
    (other.fontFeatureSettings == null || fontFeatureSettings == other.fontFeatureSettings) &&
    (other.fontSize.isUnspecified || fontSize == other.fontSize) &&
    (other.fontStyle == null || fontStyle == other.fontStyle) &&
    (other.fontSynthesis == null || fontSynthesis == other.fontSynthesis) &&
    (other.fontWeight == null || fontWeight == other.fontWeight) &&
    (other.letterSpacing.isUnspecified || letterSpacing == other.letterSpacing) &&
    (other.localeList?.let { oll -> localeList?.let { ll -> oll.containsAll(ll) } } ?: true) &&
    (other.platformStyle == null || platformStyle == other.platformStyle) &&
    (other.shadow == null || shadow == other.shadow) &&
    (other.textDecoration == null || textDecoration == other.textDecoration)
}
