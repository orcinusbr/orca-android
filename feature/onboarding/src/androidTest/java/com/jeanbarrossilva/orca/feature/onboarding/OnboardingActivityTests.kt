/*
 * Copyright © 2024 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.feature.onboarding

import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import assertk.assertThat
import assertk.assertions.isTrue
import com.jeanbarrossilva.orca.feature.onboarding.ui.test.onNextButton
import com.jeanbarrossilva.orca.feature.onboarding.ui.test.onSkipButton
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

internal class OnboardingActivityTests {
  private val composeRule = createEmptyComposeRule()
  private val activityScenarioRule = ActivityScenarioRule(OnboardingActivity::class.java)

  @get:Rule
  val ruleChain: RuleChain? = RuleChain.outerRule(composeRule).around(activityScenarioRule)

  @Test
  fun finishesOnNextButtonClick() {
    composeRule.onNextButton().performClick()
    activityScenarioRule.scenario.onActivity { assertThat(it.isFinishing).isTrue() }
  }

  @Test
  fun finishesOnSkipButtonClick() {
    composeRule.onSkipButton().performClick()
    activityScenarioRule.scenario.onActivity { assertThat(it.isFinishing).isTrue() }
  }
}
