/*
 * Copyright © 2025 Orcinus
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

package br.com.orcinus.orca.platform.autos.forms

import android.graphics.Color
import android.graphics.Outline
import android.graphics.drawable.ColorDrawable
import androidx.core.graphics.drawable.updateBounds
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import br.com.orcinus.orca.autos.forms.Form
import br.com.orcinus.orca.autos.forms.Forms
import br.com.orcinus.orca.platform.autos.Units
import br.com.orcinus.orca.platform.testing.context
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class DrawableExtensionsTests {
  @Test
  fun clips() {
    val form = Forms.default.medium as Form.PerCorner
    assertThat(ColorDrawable(Color.TRANSPARENT).apply { updateBounds(right = 1, bottom = 1) })
      .transform("clip") { it.clip(context, form) }
      .transform("getOutline") { Outline().apply(it::getOutline) }
      .prop(Outline::getRadius)
      .isEqualTo(Units.dp(context, form.topStart).toFloat())
  }
}
