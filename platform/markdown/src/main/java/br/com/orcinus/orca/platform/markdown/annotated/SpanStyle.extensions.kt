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
import android.graphics.Typeface
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.TypefaceSpan
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.text.font.resolveAsTypeface
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.isSpecified
import br.com.orcinus.orca.ext.reflection.access
import br.com.orcinus.orca.platform.markdown.spanned.span.DRAW_STYLE_SPAN_NAME
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
 * @throws NoSuchMethodException If the specified [Brush] is a [ShaderBrush] or a [DrawStyle] has
 *   been defined and the primary constructor of `androidx.compose.ui:ui-text`'s `DrawStyleSpan` or
 *   `ShaderBrushSpan` isn't found, given that they're referenced and called through reflection
 *   because both are APIs are internal to the module in which they've been declared as of 1.6.6.
 * @see SpanStyle.brush
 * @see SpanStyle.fontStyle
 * @see Typeface.NORMAL
 * @see Typeface.ITALIC
 * @see SpanStyle.drawStyle
 * @see KClass.primaryConstructor
 */
@Throws(IllegalArgumentException::class, NoSuchMethodException::class)
internal fun SpanStyle.toSpans(context: Context): List<Any> {
  return buildList {
    val foregroundColor = color.takeOrElse { (brush as? SolidColor)?.value ?: Color.Unspecified }
    if (foregroundColor.isSpecified) {
      add(ForegroundColorSpan(foregroundColor.copy(alpha = alpha).toArgb()))
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
      add(with(Density(context)) { AbsoluteSizeSpan(fontSize.roundToPx()) })
    }
    if (fontFamily != null || fontWeight != null || fontStyle != null || fontSynthesis != null) {
      add(
        TypefaceSpan(
          createFontFamilyResolver(context)
            .resolveAsTypeface(
              fontFamily,
              fontWeight ?: FontWeight.Normal,
              fontStyle ?: FontStyle.Normal,
              fontSynthesis ?: FontSynthesis.None
            )
            .value
        )
      )
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
