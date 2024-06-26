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

package br.com.orcinus.orca.platform.markdown.annotated

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color.TRANSPARENT
import android.graphics.Typeface
import android.os.Build
import android.os.LocaleList.forLanguageTags
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.TextAppearanceSpan
import android.text.style.TypefaceSpan
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.isUnspecified
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.text.font.resolveAsTypeface
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.isUnspecified
import androidx.compose.ui.util.lerp
import androidx.core.graphics.ColorUtils
import br.com.orcinus.orca.autos.colors.Colors
import br.com.orcinus.orca.ext.reflection.access
import br.com.orcinus.orca.platform.markdown.spanned.span.DRAW_STYLE_SPAN_NAME
import br.com.orcinus.orca.platform.markdown.spanned.span.createTextAppearanceSpan
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.style.Style
import java.net.URI
import java.net.URISyntaxException
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

/**
 * Denotes the start of a [SpanStyle]'s font feature setting category created from a [Style.Link].
 *
 * @see CATEGORY_SUFFIX
 */
private const val CATEGORY_PREFIX = "category: url("

/**
 * Denotes the end of a [SpanStyle]'s font feature setting category created from a [Style.Link].
 *
 * @see CATEGORY_PREFIX
 */
private const val CATEGORY_SUFFIX = ")"

/** Whether this [SpanStyle] has been created for a [Style.Link]. */
internal val SpanStyle.isForLink
  get() = fontFeatureSettings?.let { CATEGORY_PREFIX in it } ?: false

/**
 * [URI] that has been attached as the category of this [SpanStyle].
 *
 * @throws IllegalStateException If the font feature settings haven't been defined, no [category] is
 *   specified or a call within it to `url` containing the [URI] is missing.
 * @see SpanStyle.fontFeatureSettings
 */
internal val SpanStyle.uri
  @JvmName("getURI")
  @Throws(IllegalStateException::class, URISyntaxException::class)
  get() =
    fontFeatureSettings
      ?.substringAfter(CATEGORY_PREFIX, missingDelimiterValue = "")
      ?.substringBefore(CATEGORY_SUFFIX)
      ?.ifEmpty { throw IllegalStateException("No category has been specified.") }
      ?.trim()
      ?.ifEmpty { throw IllegalStateException("Category with missing call to \"url\".") }
      ?.let(::URI)
      ?: throw IllegalStateException("Font feature settings haven't been defined.")

/**
 * [SpanStyle] into which an emboldened portion within [Markdown] will be turned when converting it
 * into an [AnnotatedString].
 *
 * @see Style.Bold
 * @see toAnnotatedString
 */
@JvmField val BoldSpanStyle = SpanStyle(fontWeight = FontWeight.Bold)

/**
 * [SpanStyle] into which an italicized portion within [Markdown] will be turned when converting it
 * into an [AnnotatedString].
 *
 * @see Style.Italic
 * @see toAnnotatedString
 */
@JvmField val ItalicSpanStyle = SpanStyle(fontStyle = FontStyle.Italic)

/**
 * Creates a [SpanStyle] into which a [Style.Link] within [Markdown] will be turned when converting
 * it into an [AnnotatedString].
 *
 * @param colors [Colors] by which the [SpanStyle] can be colored.
 * @param uri [URI] to which the [Style.Link] links.
 * @see toAnnotatedString
 * @see category
 */
fun createLinkSpanStyle(colors: Colors, uri: URI): SpanStyle {
  return SpanStyle(
    Color(colors.link),
    fontFeatureSettings = CATEGORY_PREFIX + uri + CATEGORY_SUFFIX
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

/**
 * Converts this [SpanStyle] into spans.
 *
 * @param context [Context] with which conversion from a font size into density-dependent pixels is
 *   performed when the [TextUnit] is specified.
 * @throws IllegalArgumentException If the specified [Brush] isn't a [SolidColor] nor a
 *   [ShaderBrush], since there aren't equivalent spans for [Brush]es other than those of such
 *   types; font feature settings, letter spacing, a [LocaleList] or a [Shadow] has been defined but
 *   a font size hasn't; or the given [FontStyle] is neither normal nor italic.
 * @throws NoSuchFieldException If system version is at least Upside-Down Cake (API level 34), a
 *   font-specific value ([SpanStyle.fontWeight], [SpanStyle.fontStyle], [SpanStyle.fontSynthesis],
 *   [SpanStyle.fontFamily]) is non-`null` and the property to which the specified font feature
 *   settings would be assigned of the resulting [TextAppearanceSpan] isn't found.
 * @throws NoSuchMethodException If the specified [Brush] is a [ShaderBrush] or a [DrawStyle] has
 *   been defined and the primary constructor of `androidx.compose.ui:ui-text`'s `ShaderBrushSpan`
 *   or `DrawStyleSpan` isn't found.
 * @see SpanStyle.brush
 * @see SpanStyle.fontFeatureSettings
 * @see SpanStyle.letterSpacing
 * @see SpanStyle.localeList
 * @see SpanStyle.shadow
 * @see SpanStyle.fontStyle
 * @see FontStyle.Companion.Normal
 * @see FontStyle.Companion.Italic
 * @see SpanStyle.drawStyle
 * @see KClass.primaryConstructor
 */
@Throws(IllegalArgumentException::class, NoSuchFieldException::class, NoSuchMethodException::class)
internal fun SpanStyle.toSpans(context: Context): List<Any> {
  return buildList {
    val brush = brush
    val foregroundColor =
      if (color.isSpecified) {
        color
      } else if (brush is SolidColor) {
        brush.value
      } else {
        Color.Unspecified
      }
    val foregroundColorInArgb =
      if (foregroundColor.isSpecified) {
        foregroundColor.toArgb()
      } else {
        null
      }
    val typeface =
      if (fontWeight != null || fontStyle != null || fontSynthesis != null || fontFamily != null) {
        createFontFamilyResolver(context)
          .resolveAsTypeface(
            fontFamily,
            fontWeight ?: FontWeight.Normal,
            fontStyle ?: FontStyle.Normal,
            fontSynthesis ?: FontSynthesis.None
          )
          .value
      } else {
        null
      }
    val density = Density(context)
    val fontSizeInPixels =
      if (fontSize.isSpecified) {
        with(density) { fontSize.roundToPx() }
      } else {
        null
      }
    if (foregroundColorInArgb != null) {
      add(
        ForegroundColorSpan(
          ColorUtils.setAlphaComponent(
            foregroundColorInArgb,
            lerp(start = 0, stop = 255, fraction = alpha)
          )
        )
      )
    } else if (brush is ShaderBrush) {
      Class.forName("androidx.compose.ui.text.platform.style.ShaderBrushSpan")
        .kotlin
        .primaryConstructor
        ?.access { call(brush) }
        ?.run(::add)
        ?: throw NoSuchMethodException("ShaderBrushSpan.<init>(ShaderBrush)")
    } else if (brush != null) {
      throw IllegalArgumentException("No equivalent spans for non-solid-color and -shader brushes.")
    }
    fontSizeInPixels?.let { add(AbsoluteSizeSpan(it)) }
    typeface?.let {
      if (fontSizeInPixels != null) {
        if (foregroundColorInArgb == null) {
          throw IllegalArgumentException(
            "A color must be specified in order for a span containing the font size to be created."
          )
        } else {
          val foregroundColorStateList = ColorStateList.valueOf(foregroundColorInArgb)
          add(
            createTextAppearanceSpan(
              family =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                  typeface.systemFontFamilyName
                } else {
                  null
                },
              when (fontStyle) {
                FontStyle.Normal,
                null -> Typeface.NORMAL
                FontStyle.Italic -> Typeface.ITALIC
                else ->
                  throw IllegalArgumentException(
                    "Font style must be either ${FontStyle.Normal} or ${FontStyle.Italic}, but " +
                      "was $fontStyle instead."
                  )
              },
              fontSizeInPixels,
              foregroundColorStateList,
              textColorLink = foregroundColorStateList,
              fontWeight?.weight ?: typeface.weight,
              localeList
                ?.joinToString(separator = ",", transform = Locale::toLanguageTag)
                ?.let(::forLanguageTags),
              typeface,
              shadow?.blurRadius ?: Float.NaN,
              shadow?.offset?.x ?: Float.NaN,
              shadow?.offset?.y ?: Float.NaN,
              shadow?.color?.toArgb() ?: TRANSPARENT,
              hasElegantTextHeight = false,
              if (letterSpacing.isSpecified) with(density) { letterSpacing.toPx() } else Float.NaN,
              fontFeatureSettings,
              fontVariationSettings = null
            )
          )
        }
      } else if (fontFeatureSettings != null) {
        missingFontSizeFor("font feature settings")
      } else if (letterSpacing.isSpecified) {
        missingFontSizeFor("letter spacing")
      } else if (localeList != null) {
        missingFontSizeFor("locale list")
      } else if (shadow != null && shadow != Shadow.None) {
        missingFontSizeFor("shadow")
      } else {
        add(TypefaceSpan(typeface))
      }
    }
    if (background.isSpecified) {
      add(BackgroundColorSpan(background.toArgb()))
    }
    when (baselineShift) {
      BaselineShift.Superscript -> add(SuperscriptSpan())
      BaselineShift.Subscript -> add(SubscriptSpan())
    }
    drawStyle?.let {
      Class.forName(DRAW_STYLE_SPAN_NAME).kotlin.primaryConstructor?.access { call(it) }?.run(::add)
        ?: throw NoSuchMethodException("DrawStyleSpan.<init>(DrawStyle)")
    }
  }
}

/**
 * Throws an [IllegalArgumentException] stating that a given [SpanStyle] property can only have a
 * span created for it when a font size has been specified.
 *
 * @param propertyDescription Short description of the property that requires the font size.
 * @see SpanStyle.fontSize
 * @see TextUnit.isSpecified
 */
private fun missingFontSizeFor(propertyDescription: String): Nothing {
  throw IllegalArgumentException(
    "A font size must be specified in order for a span containing a $propertyDescription to be " +
      "created."
  )
}
