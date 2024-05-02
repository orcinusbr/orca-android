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

import android.graphics.Typeface
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan
import android.text.style.TypefaceSpan
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.PlatformSpanStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.sp
import androidx.core.text.getSpans
import assertk.all
import assertk.assertThat
import br.com.orcinus.orca.platform.markdown.spanned.span.areStructurallyEqual
import br.com.orcinus.orca.platform.markdown.test.R
import br.com.orcinus.orca.platform.testing.context
import kotlin.test.Test

internal class AnnotatedStringExtensionsTests {
  @Test
  fun convertsIntoEditable() {
    assertThat(
        buildAnnotatedString {
            withStyle(
              SpanStyle(
                Color.Blue,
                fontSize = 2.sp,
                FontWeight.Medium,
                FontStyle.Italic,
                FontSynthesis.Weight,
                FontFamily.Default,
                fontFeatureSettings = "normal"
              )
            ) {
              append("Hello")
            }
            append(", ")
            withStyle(
              SpanStyle(
                letterSpacing = 4.sp,
                baselineShift = BaselineShift.Subscript,
                textGeometricTransform = TextGeometricTransform(scaleX = 16f),
                localeList = LocaleList(android.os.LocaleList.getDefault().toLanguageTags()),
                background = Color.Gray,
                textDecoration = TextDecoration.LineThrough,
                shadow = Shadow(Color.Black, Offset(x = 0f, y = 256f)),
                platformStyle = PlatformSpanStyle.Default,
                drawStyle = Stroke()
              )
            ) {
              append("world")
            }
            append('!')
          }
          .toEditableAsState(context)
          .value
      )
      .all {
        transform { it.getSpans<Any>(start = 0, end = 4) }
          .given {
            it.forEachIndexed { index, span ->
              span.areStructurallyEqual(
                context,
                when (index) {
                  0 -> ForegroundColorSpan(android.graphics.Color.BLUE)
                  1 -> with(Density(context)) { AbsoluteSizeSpan(2.sp.roundToPx()) }
                  2 -> TypefaceSpan(Typeface.create(Typeface.DEFAULT, FontWeight.Bold.weight, true))
                  3 -> TextAppearanceSpan(context, R.style.NormalFontFeatureSetttingsTextAppearance)
                  else -> object {}
                }
              )
            }
          }
      }
  }
}
