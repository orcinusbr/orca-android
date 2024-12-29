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

package br.com.orcinus.orca.composite.timeline.search.field.interop

import android.view.View
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import br.com.orcinus.orca.platform.testing.context
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class PaddingsTests {
  @Test
  fun setsViewPadding() {
    val density = Density(context)
    val startPaddingInDp = 0.dp
    val topPaddingInDp = 2.dp
    val endPaddingInDp = 4.dp
    val bottomPaddingInDp = 16.dp
    val view =
      View(context).apply {
        setPadding(
          PaddingValues(startPaddingInDp, topPaddingInDp, endPaddingInDp, bottomPaddingInDp)
        )
      }
    assertThat(view).all {
      prop(View::getPaddingStart).isEqualTo(with(density) { startPaddingInDp.roundToPx() })
      prop(View::getPaddingTop).isEqualTo(with(density) { topPaddingInDp.roundToPx() })
      prop(View::getPaddingEnd).isEqualTo(with(density) { endPaddingInDp.roundToPx() })
      prop(View::getPaddingBottom).isEqualTo(with(density) { bottomPaddingInDp.roundToPx() })
    }
  }
}
