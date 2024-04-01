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

import android.content.res.AssetManager
import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.compose.ui.unit.dp
import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import br.com.orcinus.orca.platform.testing.context
import io.mockk.mockkObject
import io.mockk.verify
import kotlin.reflect.KFunction
import kotlin.reflect.full.staticFunctions
import org.junit.Test

internal class ScreenTests {
  @Test
  fun createsScreenFromResources() {
    assertThat(
        Screen.from(
          @Suppress("DEPRECATION")
          Resources(
              AssetManager::class
                .staticFunctions
                .filterIsInstance<KFunction<AssetManager>>()
                .single { it.name == "getSystem" }
                .call(),
              DisplayMetrics(),
              Configuration()
            )
            .apply {
              displayMetrics.widthPixels = 2
              displayMetrics.heightPixels = 8
              configuration.screenWidthDp = 4
              configuration.screenHeightDp = 16
            }
        )
      )
      .all {
        prop("width.inPixels") { it.width.inPixels }.isEqualTo(2)
        prop("width.inDps") { it.width.inDps }.isEqualTo(4.dp)
        prop("height.inPixels") { it.height.inPixels }.isEqualTo(8)
        prop("height.inDps") { it.height.inDps }.isEqualTo(16.dp)
      }
  }

  @Test
  fun creatingScreenFromContextDelegatesToDoingSoWithItsResources() {
    mockkObject(Screen.Companion) {
      Screen.from(context)
      verify { Screen.from(context.resources) }
    }
  }
}
