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

package br.com.orcinus.orca.platform.autos.kit.input.text.markdown.interop

import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.prop
import br.com.orcinus.orca.platform.testing.context
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class StateExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun statefulDrawableHasSameSizeAsComposable() {
    var drawable: Drawable? = null
    val size = 2.dp
    val sizeInPixels =
      TypedValue.applyDimension(
          TypedValue.COMPLEX_UNIT_DIP,
          size.value,
          context.resources.displayMetrics
        )
        .toInt()
    composeRule.setContent { drawable = drawableStateOf { Spacer(Modifier.size(size)) }.value }
    assertThat(drawable).isNotNull().all {
      prop(Drawable::getIntrinsicWidth).isEqualTo(sizeInPixels)
      prop(Drawable::getIntrinsicHeight).isEqualTo(sizeInPixels)
    }
  }
}
