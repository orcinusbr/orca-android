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

@file:JvmName("ParcelableSpanExtensions")

package br.com.orcinus.orca.platform.markdown.span

import android.content.Context
import android.os.Build
import android.text.ParcelableSpan
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.LocaleSpan
import android.text.style.ScaleXSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.TextAppearanceSpan
import android.text.style.TypefaceSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Typeface
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

/**
 * Qualified name of `androidx.compose.ui:ui-text`'s `DrawStyleSpan` [ParcelableSpan] as of 1.6.6.
 */
internal const val DRAW_STYLE_SPAN_NAME = "androidx.compose.ui.text.platform.style.DrawStyleSpan"

/**
 * Compares both [ParcelableSpan]s structurally.
 *
 * @param other [ParcelableSpan] to which the receiver one will be compared.
 */
fun ParcelableSpan.isStructurallyEqualTo(other: ParcelableSpan): Boolean {
  return when {
    this is StyleSpan && other is StyleSpan -> isStructurallyEqualTo(other)
    this is URLSpan && other is URLSpan -> url == other.url
    else -> spanTypeId == other.spanTypeId
  }
}

/**
 * Converts this [ParcelableSpan] into a [SpanStyle].
 *
 * @param context [Context] with which conversion from either density-dependent or -independent
 *   pixels into scalable ones is performed when the receiver is an [AbsoluteSizeSpan].
 * @throws NoSuchFieldException If the receiver is an `androidx.compose.ui:ui-text` `DrawStyleSpan`
 *   but doesn't have a declared member property to which a [DrawStyle] is assigned.
 */
@Throws(NoSuchFieldException::class)
internal fun ParcelableSpan.toSpanStyle(context: Context): SpanStyle {
  return SpanStyle(
    color =
      if (this is ForegroundColorSpan) {
        Color(foregroundColor)
      } else {
        Color.Unspecified
      },
    fontSize =
      if (this is AbsoluteSizeSpan) {
        with(Density(context)) {
          if (dip) {
            size.dp.toSp()
          } else {
            size.toSp()
          }
        }
      } else {
        TextUnit.Unspecified
      },
    fontWeight =
      if (this is TypefaceSpan) {
        typeface?.weight?.let {
          when {
            it <= 0 -> null
            it <= 100 -> FontWeight.Thin
            it <= 200 -> FontWeight.ExtraLight
            it <= 300 -> FontWeight.Light
            it <= 400 -> FontWeight.Normal
            it <= 500 -> FontWeight.Medium
            it <= 600 -> FontWeight.SemiBold
            it <= 700 -> FontWeight.Bold
            else -> FontWeight.Black
          }
        }
      } else {
        null
      },
    fontStyle =
      if (this is StyleSpan && style and android.graphics.Typeface.ITALIC != 0) {
        FontStyle.Italic
      } else {
        null
      },
    fontFamily =
      if (this is TypefaceSpan) {
        typeface?.let(::Typeface)?.fontFamily
      } else {
        null
      },
    fontFeatureSettings =
      if (this is TextAppearanceSpan) {
        fontFeatureSettings
      } else {
        null
      },
    letterSpacing =
      if (this is TextAppearanceSpan) {
        letterSpacing.em
      } else {
        TextUnit.Unspecified
      },
    baselineShift =
      when (this) {
        is SuperscriptSpan -> BaselineShift.Superscript
        is SubscriptSpan -> BaselineShift.Subscript
        else -> null
      },
    textGeometricTransform =
      if (this is ScaleXSpan) {
        TextGeometricTransform(scaleX)
      } else {
        null
      },
    localeList =
      if (this is LocaleSpan) {
        LocaleList(List(locales.size()) { Locale(locales[it].toLanguageTag()) })
      } else {
        null
      },
    background =
      if (this is BackgroundColorSpan) {
        Color(backgroundColor)
      } else {
        Color.Unspecified
      },
    textDecoration =
      when (this) {
        is UnderlineSpan -> TextDecoration.Underline
        is StrikethroughSpan -> TextDecoration.LineThrough
        else -> null
      },
    shadow =
      if (this is TextAppearanceSpan) {
        Shadow(Color(shadowColor), Offset(shadowDx, shadowDy), shadowRadius)
      } else {
        null
      },
    drawStyle =
      with(this::class) {
        if (qualifiedName == DRAW_STYLE_SPAN_NAME) {
          declaredMemberProperties
            .filterIsInstance<KProperty1<ParcelableSpan, DrawStyle>>()
            .singleOrNull()
            ?.get(this@toSpanStyle)
            ?: throw NoSuchFieldException("DrawStyleSpan.drawStyle")
        } else {
          null
        }
      }
  )
}

/**
 * Compares both [StyleSpan]s structurally.
 *
 * @param other [StyleSpan] to which the receiver one will be compared.
 */
private fun StyleSpan.isStructurallyEqualTo(other: StyleSpan): Boolean {
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    style == other.style && fontWeightAdjustment == other.fontWeightAdjustment
  } else {
    style == other.style
  }
}
