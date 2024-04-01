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

package br.com.orcinus.orca.platform.testing.activity.scenario

import android.app.Activity
import androidx.test.ext.junit.rules.ActivityScenarioRule
import assertk.assertThat
import assertk.assertions.isSameAs
import org.junit.Rule
import org.junit.Test

internal class ActivityScenarioExtensionsTests {
  @get:Rule val activityScenarioRule = ActivityScenarioRule(Activity::class.java)

  @Test
  fun getsActivity() {
    activityScenarioRule.scenario?.run { onActivity { assertThat(activity).isSameAs(it) } }
  }
}
