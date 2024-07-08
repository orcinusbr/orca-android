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

@file:JvmName("AnyExtensions")

package br.com.orcinus.orca.platform.autos.kit.input.text.composition.interop.spanned.span

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface.BOLD
import android.graphics.Typeface.BOLD_ITALIC
import android.graphics.Typeface.ITALIC
import android.graphics.fonts.FontStyle.FONT_WEIGHT_MAX
import android.graphics.fonts.FontStyle.FONT_WEIGHT_MIN
import android.os.Build
import android.text.ParcelableSpan
import android.text.Spanned
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
import android.util.TypedValue
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
import br.com.orcinus.orca.std.markdown.style.Style
import java.net.URI
import kotlin.math.roundToInt
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

/**
 * Possible states for which a [ColorStateList] can provide a color (according to the
 * [documentation](https://developer.android.com/guide/topics/resources/color-list-resource)).
 *
 * @see ColorStateList.getColorForState
 */
private val colorStateListStates =
  intArrayOf(
    android.R.attr.state_checkable,
    -android.R.attr.state_checkable,
    android.R.attr.state_checked,
    -android.R.attr.state_checked,
    android.R.attr.state_enabled,
    -android.R.attr.state_enabled,
    android.R.attr.state_pressed,
    -android.R.attr.state_pressed,
    android.R.attr.state_selected,
    -android.R.attr.state_selected,
    android.R.attr.state_window_focused,
    -android.R.attr.state_window_focused,
  )

/** Qualified name of `androidx.compose.ui:ui-text`'s `DrawStyleSpan` as of 1.6.6. */
internal const val DRAW_STYLE_SPAN_NAME = "androidx.compose.ui.text.platform.style.DrawStyleSpan"

/**
 * Compares both spans structurally.
 *
 * @param context [Context] with which both font sizes will be converted to the same unit when both
 *   the receiver and [other] are [AbsoluteSizeSpan]s.
 * @param other Span to which the receiver one will be compared.
 */
fun Any.isStructurallyEqual(context: Context, other: Any): Boolean {
  return when {
    this is AbsoluteSizeSpan && other is AbsoluteSizeSpan -> isStructurallyEqual(context, other)
    this is StyleSpan && other is StyleSpan -> isStructurallyEqual(other)
    this is TextAppearanceSpan && other is TextAppearanceSpan -> isStructurallyEqual(other)
    this is URLSpan && other is URLSpan -> url == other.url
    this is ParcelableSpan && other is ParcelableSpan -> spanTypeId == other.spanTypeId
    else -> this == other
  }
}

/**
 * Converts this span into a [SpanStyle].
 *
 * @param context [Context] with which conversion from either density-dependent or -independent
 *   pixels into scalable ones is performed when the receiver is an [AbsoluteSizeSpan].
 * @throws NoSuchFieldException If the receiver is an `androidx.compose.ui:ui-text` `DrawStyleSpan`
 *   but doesn't have a declared member property to which a [DrawStyle] is assigned.
 */
@Throws(NoSuchFieldException::class)
internal fun Any.toSpanStyle(context: Context): SpanStyle {
  val typeface =
    when {
      this is TextAppearanceSpan && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> typeface
      this is TypefaceSpan -> typeface
      else -> null
    }
  val fontWeight =
    if (this is TextAppearanceSpan && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      textFontWeight
    } else {
      typeface?.weight
    }
  val style =
    if (this is StyleSpan) {
      style
    } else {
      typeface?.style
    }
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
      fontWeight
        ?.coerceIn(
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) FONT_WEIGHT_MIN else 1,
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) FONT_WEIGHT_MAX else 1_000
        )
        ?.let(::FontWeight),
    fontStyle =
      if (style != null && style and ITALIC != 0) {
        FontStyle.Italic
      } else {
        null
      },
    fontFamily = typeface?.let(::Typeface)?.fontFamily,
    fontFeatureSettings =
      if (this is TextAppearanceSpan && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        fontFeatureSettings
      } else {
        null
      },
    letterSpacing =
      if (
        this is TextAppearanceSpan && Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE
      ) {
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
      if (this is TextAppearanceSpan && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        Shadow(Color(shadowColor), Offset(shadowDx, shadowDy), shadowRadius)
      } else {
        null
      },
    drawStyle =
      with(this::class) {
        if (qualifiedName == DRAW_STYLE_SPAN_NAME) {
          declaredMemberProperties
            .filterIsInstance<KProperty1<Any, DrawStyle>>()
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
 * Converts this span into [Style]s.
 *
 * @param indices Indices of a [Spanned] to which this span has been applied.
 * @throws IllegalArgumentException If this is a [URLSpan] and the amount of indices doesn't match
 *   the length of the URL.
 * @see URLSpan.getURL
 */
internal fun Any.toStyles(indices: IntRange): List<Style> {
  return when (this) {
    is StyleSpan -> toStyles(indices)
    is URLSpan -> listOf(Style.Link(URI(url), indices))
    else -> emptyList()
  }
}

/**
 * Compares both [AbsoluteSizeSpan]s structurally.
 *
 * @param context [Context] with which both font sizes will be converted to the same unit.
 * @param other [AbsoluteSizeSpan] to which the receiver one will be compared.
 * @see AbsoluteSizeSpan.getSize
 */
private fun AbsoluteSizeSpan.isStructurallyEqual(
  context: Context,
  other: AbsoluteSizeSpan
): Boolean {
  val px: (Int) -> Int = {
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        it.toFloat(),
        context.resources?.displayMetrics
      )
      .roundToInt()
  }
  return when {
    dip == other.dip -> size == size
    dip && !other.dip -> px(size) == other.size
    else -> size == px(other.size)
  }
}

/**
 * Compares both [StyleSpan]s structurally.
 *
 * @param other [StyleSpan] to which the receiver one will be compared.
 */
private fun StyleSpan.isStructurallyEqual(other: StyleSpan): Boolean {
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    style == other.style && fontWeightAdjustment == other.fontWeightAdjustment
  } else {
    style == other.style
  }
}

/**
 * Compares both [TextAppearanceSpan]s structurally.
 *
 * @param other [TextAppearanceSpan] to which the receiver one will be compared.
 */
private fun TextAppearanceSpan.isStructurallyEqual(other: TextAppearanceSpan): Boolean {
  return family == other.family &&
    textColor.isStructurallyEqual(other.textColor) &&
    linkTextColor.isStructurallyEqual(other.linkTextColor) &&
    textSize == other.textSize &&
    textStyle == other.textStyle &&
    (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q ||
      textFontWeight == other.textFontWeight &&
        textLocales == other.textLocales &&
        shadowColor == other.shadowColor &&
        (shadowDx.isNaN() && shadowDy.isNaN() || shadowDx == other.shadowDx) &&
        (shadowDy.isNaN() && shadowDy.isNaN() || shadowDy == other.shadowDy) &&
        (shadowRadius.isNaN() && other.shadowRadius.isNaN() ||
          shadowRadius == other.shadowRadius) &&
        fontFeatureSettings == other.fontFeatureSettings &&
        fontVariationSettings == other.fontVariationSettings &&
        isElegantTextHeight == other.isElegantTextHeight) &&
    (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE ||
      typeface?.systemFontFamilyName == other.typeface?.systemFontFamilyName &&
        letterSpacing == other.letterSpacing)
}

/**
 * Compares both [ColorStateList]s structurally.
 *
 * @param other [ColorStateList] to which the receiver one will be compared.
 */
private fun ColorStateList.isStructurallyEqual(other: ColorStateList): Boolean {
  return if (isStateful) {
    for (state in colorStateListStates) {
      if (
        getColorForState(intArrayOf(state), defaultColor) !=
          other.getColorForState(intArrayOf(state), other.defaultColor)
      ) {
        return false
      }
    }
    true
  } else {
    defaultColor == other.defaultColor
  }
}

/**
 * Converts this [StyleSpan] into [Style]s.
 *
 * @param indices Indices of a [Spanned] to which this [StyleSpan] has been applied.
 */
private fun StyleSpan.toStyles(indices: IntRange): List<Style> {
  return when (style) {
    BOLD -> listOf(Style.Bold(indices))
    BOLD_ITALIC -> listOf(Style.Bold(indices), Style.Italic(indices))
    ITALIC -> listOf(Style.Italic(indices))
    else -> emptyList()
  }
}
