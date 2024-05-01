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

import android.text.ParcelableSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.BaselineShift
import br.com.orcinus.orca.ext.reflection.access
import br.com.orcinus.orca.platform.markdown.spanned.span.DRAW_STYLE_SPAN_NAME
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

/**
 * Converts this [SpanStyle] into [ParcelableSpan]s.
 *
 * @throws IllegalArgumentException If the specified [Brush] isn't a [SolidColor] nor a
 *   [ShaderBrush], since there aren't equivalent [ParcelableSpan]s for [Brush]es other than those
 *   of such types.
 * @throws NoSuchMethodException If the specified [Brush] is a [ShaderBrush] or a [DrawStyle] has
 *   been defined and the primary constructor of `androidx.compose.ui:ui-text`'s `DrawStyleSpan` or
 *   `ShaderBrushSpan` isn't found, given that they're referenced and called through reflection
 *   because both are APIs are internal to the module in which they've been declared as of 1.6.6.
 * @see SpanStyle.brush
 * @see SpanStyle.drawStyle
 * @see KClass.primaryConstructor
 */
@Throws(IllegalArgumentException::class, NoSuchMethodException::class)
internal fun SpanStyle.toParcelableSpans(): List<ParcelableSpan> {
  return buildList {
    val foregroundColor = color.takeOrElse { (brush as? SolidColor)?.value ?: Color.Unspecified }
    if (foregroundColor.isSpecified) {
      add(ForegroundColorSpan(foregroundColor.copy(alpha = alpha).toArgb()))
    } else if (brush is ShaderBrush) {
      Class.forName("androidx.compose.ui.text.platform.style.ShaderBrushSpan")
        .kotlin
        .primaryConstructor
        ?.access { call(brush) as ParcelableSpan }
        ?.run(::add)
        ?: throw NoSuchMethodException("ShaderBrushSpan.<init>(ShaderBrush)")
    } else if (brush != null) {
      throw IllegalArgumentException("No equivalent spans for non-solid-color and -shader brushes.")
    }
    if (background.isSpecified) {
      add(BackgroundColorSpan(background.toArgb()))
    }
    when (baselineShift) {
      BaselineShift.Superscript -> add(SuperscriptSpan())
      BaselineShift.Subscript -> add(SubscriptSpan())
    }
    drawStyle?.let {
      Class.forName(DRAW_STYLE_SPAN_NAME)
        .kotlin
        .primaryConstructor
        ?.access { call(it) as ParcelableSpan }
        ?.run(::add)
        ?: throw NoSuchMethodException("DrawStyleSpan.<init>(DrawStyle)")
    }
  }
}
