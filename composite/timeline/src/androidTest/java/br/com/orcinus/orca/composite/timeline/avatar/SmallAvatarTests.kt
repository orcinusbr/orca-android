/*
 * Copyright © 2023-2024 Orca
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

package br.com.orcinus.orca.composite.timeline.avatar

import androidx.compose.ui.test.junit4.createComposeRule
import br.com.orcinus.orca.composite.timeline.avatar.test.onAvatar
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.loadable.placeholder.test.assertIsLoading
import com.jeanbarrossilva.loadable.placeholder.test.assertIsNotLoading
import org.junit.Rule
import org.junit.Test

internal class SmallAvatarTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun isTaggedWhenEmpty() {
    composeRule.setContent { AutosTheme { SmallAvatar() } }
    composeRule.onAvatar().assertExists()
  }

  @Test
  fun isLoadingWhenEmpty() {
    composeRule.setContent { AutosTheme { SmallAvatar() } }
    composeRule.onAvatar().assertIsLoading()
  }

  @Test
  fun isTaggedWhenPopulated() {
    composeRule.setContent { AutosTheme { SampleSmallAvatar() } }
    composeRule.onAvatar().assertExists()
  }

  @Test
  fun isLoadedWhenPopulated() {
    composeRule.setContent { AutosTheme { SampleSmallAvatar() } }
    composeRule.onAvatar().assertIsNotLoading()
  }
}
