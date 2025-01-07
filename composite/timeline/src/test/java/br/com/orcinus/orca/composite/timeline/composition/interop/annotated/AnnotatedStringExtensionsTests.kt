/*
 * Copyright © 2024–2025 Orcinus
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

package br.com.orcinus.orca.composite.timeline.composition.interop.annotated

import android.content.res.ColorStateList
import android.graphics.Color.BLUE
import android.graphics.Color.TRANSPARENT
import android.graphics.Typeface
import android.graphics.fonts.FontStyle.FONT_WEIGHT_MEDIUM
import android.os.Build
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.sp
import androidx.core.text.getSpans
import assertk.all
import assertk.assertThat
import br.com.orcinus.orca.composite.timeline.composition.interop.spanned.span.createTextAppearanceSpan
import br.com.orcinus.orca.platform.testing.context
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
internal class AnnotatedStringExtensionsTests {
  @Config(sdk = [Build.VERSION_CODES.TIRAMISU])
  @Test
  fun convertsIntoEditable() {
    val density = Density(context)
    val fontSize = 2.sp
    val fontSizeInPixels = with(density) { fontSize.roundToPx() }
    assertThat(
        buildAnnotatedString {
            withStyle(
              SpanStyle(
                Color.Blue,
                fontSize,
                FontWeight.Medium,
                FontStyle.Italic,
                FontSynthesis.Weight,
                FontFamily.Default,
                fontFeatureSettings = "normal"
              )
            ) {
              append("Hello")
            }
            append(", world!")
          }
          .toEditableAsState(context)
          .value
      )
      .all {
        transform { it.getSpans<Any>(start = 0, end = 5) }
          .areStructurallyEqual(
            ForegroundColorSpan(BLUE),
            AbsoluteSizeSpan(fontSizeInPixels),
            createTextAppearanceSpan(
              family = null,
              style = Typeface.ITALIC,
              fontSizeInPixels,
              ColorStateList.valueOf(BLUE),
              textColorLink = ColorStateList.valueOf(TRANSPARENT),
              FONT_WEIGHT_MEDIUM,
              textLocales = null,
              Typeface.DEFAULT,
              shadowRadius = Float.NaN,
              shadowDx = Float.NaN,
              shadowDy = Float.NaN,
              shadowColor = TRANSPARENT,
              hasElegantTextHeight = false,
              letterSpacing = Float.NaN,
              fontFeatureSettings = "normal",
              fontVariationSettings = null
            )
          )
      }
  }
}
