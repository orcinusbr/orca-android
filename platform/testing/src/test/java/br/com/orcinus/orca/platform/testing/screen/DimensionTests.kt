/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.platform.testing.screen

import androidx.compose.ui.unit.dp
import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.platform.testing.context
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class DimensionTests {
  @Test
  fun createsWidthFromResources() {
    val width = Screen.Dimension.width(context.resources)
    assertThat(width.inPixels).isEqualTo(context.resources.displayMetrics.widthPixels)
    assertThat(width.inDps).isEqualTo(context.resources.configuration.screenWidthDp.dp)
  }

  @Test
  fun createsHeightFromResources() {
    val height = Screen.Dimension.height(context.resources)
    assertThat(height.inPixels).isEqualTo(context.resources.displayMetrics.heightPixels)
    assertThat(height.inDps).isEqualTo(context.resources.configuration.screenHeightDp.dp)
  }
}
