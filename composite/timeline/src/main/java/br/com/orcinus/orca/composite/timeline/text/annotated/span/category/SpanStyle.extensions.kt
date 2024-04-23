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

@file:JvmName("SpanStyles")

package br.com.orcinus.orca.composite.timeline.text.annotated.span.category

import androidx.compose.ui.text.SpanStyle
import br.com.orcinus.orca.composite.timeline.text.annotated.span.createLinkSpanStyle
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.std.markdown.style.type.Email
import br.com.orcinus.orca.std.markdown.style.type.Hashtag
import br.com.orcinus.orca.std.markdown.style.type.Link
import br.com.orcinus.orca.std.markdown.style.type.Mention
import java.net.MalformedURLException
import java.net.URL

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
      ?.substringAfter(Categorizer.PREFIX, missingDelimiterValue = "")
      ?.substringBefore(" ")
      ?.ifEmpty { null }
      ?.trim()
      ?: error("No category has been attached to $fontFeatureSettings.")

/** Whether this [SpanStyle] has been created for an [Email]. */
internal val SpanStyle.isForEmail
  get() = fontFeatureSettings?.let { Categorizer.categorizeAsEmail() in it } ?: false

/** Whether this [SpanStyle] has been created for a [Hashtag]. */
internal val SpanStyle.isForHashtag
  get() = fontFeatureSettings?.let { Categorizer.HASHTAG_SPEC_PREFIX in it } ?: false

/** Whether this [SpanStyle] has been created for a [Link]. */
internal val SpanStyle.isForLink
  get() = fontFeatureSettings?.let { Categorizer.LINK_SPEC_START in it } ?: false

/** Whether this [SpanStyle] has been created for a [Mention]. */
internal val SpanStyle.isForMention
  get() = fontFeatureSettings?.let { Categorizer.MENTION_SPEC_PREFIX in it } ?: false

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
      .substringAfter("${Categorizer.MENTION_TAG} ", missingDelimiterValue = "")
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
      .substringAfter(Categorizer.LINK_SPEC_START, missingDelimiterValue = "")
      .substringBeforeLast(Categorizer.LINK_SPEC_END, missingDelimiterValue = "")
      .ifEmpty {
        throw MalformedURLException("Metadata with missing or malformed call to \"url\".")
      }
      .let(::URL)
