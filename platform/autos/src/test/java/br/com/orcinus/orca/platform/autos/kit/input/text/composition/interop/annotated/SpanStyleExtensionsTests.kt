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

package br.com.orcinus.orca.platform.autos.kit.input.text.composition.interop.annotated

import android.graphics.Color.BLACK
import android.graphics.Color.argb
import android.text.style.ForegroundColorSpan
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.SpanStyle
import assertk.assertThat
import br.com.orcinus.orca.platform.testing.context
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class SpanStyleExtensionsTests {
  @Test
  fun convertsSpanStyleWithSolidColorBrushIntoForegroundColorSpan() {
    assertThat(SpanStyle(SolidColor(Color.Black), alpha = .8f).toSpans(context).toTypedArray())
      .areStructurallyEqual(ForegroundColorSpan(argb(.8f, 0f, 0f, 0f)))
  }

  @Test
  fun convertsSpanStyleWithColorIntoForegroundColorSpan() {
    assertThat(SpanStyle(Color.Black).toSpans(context).toTypedArray())
      .areStructurallyEqual(ForegroundColorSpan(BLACK))
  }
}
