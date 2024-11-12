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

package br.com.orcinus.orca.app.demo

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import br.com.orcinus.orca.app.R
import br.com.orcinus.orca.app.activity.OrcaActivity
import br.com.orcinus.orca.feature.settings.termmuting.TermMutingActivity
import br.com.orcinus.orca.platform.autos.test.kit.action.setting.onSetting
import br.com.orcinus.orca.platform.intents.test.intendStartingOf
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class SettingsTests {
  @get:Rule val composeRule = createAndroidComposeRule<OrcaActivity>()

  @BeforeTest
  fun setUp() {
    onView(withId(R.id.settings)).perform(click())
  }

  @Test
  fun navigatesToTermMuting() {
    intendStartingOf<TermMutingActivity> {
      composeRule
        .onSetting(
          composeRule.activity.getString(
            br.com.orcinus.orca.feature.settings.R.string.feature_settings_muting
          )
        )
        .performClick()
      composeRule
        .onSetting(
          composeRule.activity.getString(
            br.com.orcinus.orca.feature.settings.R.string.feature_settings_add
          )
        )
        .performClick()
    }
  }
}
