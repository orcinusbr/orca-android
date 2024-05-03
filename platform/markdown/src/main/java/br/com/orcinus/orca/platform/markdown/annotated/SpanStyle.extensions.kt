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

package br.com.orcinus.orca.platform.markdown.annotated

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color.TRANSPARENT
import android.graphics.Typeface
import android.os.Build
import android.os.LocaleList
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.text.font.resolveAsTypeface
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.util.lerp
import androidx.core.graphics.ColorUtils
import br.com.orcinus.orca.ext.reflection.access
import br.com.orcinus.orca.platform.markdown.spanned.span.DRAW_STYLE_SPAN_NAME
import br.com.orcinus.orca.platform.markdown.spanned.span.createTextAppearanceSpan
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

/**
 * Converts this [SpanStyle] into spans.
 *
 * @param context [Context] with which conversion from a font size into density-dependent pixels is
 *   performed when the [TextUnit] is specified.
 * @throws IllegalArgumentException If the specified [Brush] isn't a [SolidColor] nor a
 *   [ShaderBrush], since there aren't equivalent spans for [Brush]es other than those of such
 *   types.
 * @throws NoSuchFieldException If system version is at least Upside-Down Cake (API level 34), a
 *   font-specific value ([SpanStyle.fontWeight], [SpanStyle.fontStyle], [SpanStyle.fontSynthesis],
 *   [SpanStyle.fontFamily]) is non-`null` and the property to which the specified font feature
 *   settings would be assigned of the resulting [TextAppearanceSpan] isn't found.
 * @throws NoSuchMethodException If the specified [Brush] is a [ShaderBrush] or a [DrawStyle] has
 *   been defined and the primary constructor of `androidx.compose.ui:ui-text`'s `DrawStyleSpan` or
 *   `ShaderBrushSpan` isn't found.
 * @see SpanStyle.brush
 * @see SpanStyle.fontFeatureSettings
 * @see Typeface.NORMAL
 * @see Typeface.ITALIC
 * @see SpanStyle.drawStyle
 * @see KClass.primaryConstructor
 */
@Throws(IllegalArgumentException::class, NoSuchMethodException::class)
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
    if (fontSize.isSpecified) {
      add(with(density) { AbsoluteSizeSpan(fontSize.roundToPx()) })
    }
    typeface?.let {
      if (
        fontSizeInPixels != null && (foregroundColorInArgb != null || fontFeatureSettings != null)
      ) {
        add(
          createTextAppearanceSpan(
            family =
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                typeface.systemFontFamilyName
              } else {
                null
              },
            when (fontStyle) {
              FontStyle.Normal -> Typeface.NORMAL
              FontStyle.Italic -> Typeface.ITALIC
              else -> -1
            },
            fontSizeInPixels,
            ColorStateList.valueOf(foregroundColorInArgb ?: TRANSPARENT),
            textColorLink = ColorStateList.valueOf(TRANSPARENT),
            fontWeight?.weight ?: typeface.weight,
            localeList
              ?.joinToString(separator = ",", transform = Locale::toLanguageTag)
              ?.let(LocaleList::forLanguageTags),
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
