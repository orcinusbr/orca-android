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

package br.com.orcinus.orca.app

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import br.com.orcinus.orca.app.demo.DemoOrcaActivity
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

internal class BottomNavigationTests {
  @get:Rule val composeRule = createAndroidComposeRule<DemoOrcaActivity>()

  @Test
  fun navigatesOnceWhenClickingFeedItemMultipleTimes() {
    repeat(2) { onView(withId(R.id.feed)).perform(click()) }
    assertEquals(1, composeRule.activity.supportFragmentManager.fragments.size)
  }

  @Test
  fun navigatesOnceWhenClickingProfileDetailsItemMultipleTimes() {
    repeat(2) { onView(withId(R.id.profile_details)).perform(click()) }

    /*
     * We expect it to have two Fragment instances because we start with a FeedFragment,
     * and then comes the single ProfileDetailsFragment one after the navigation is
     * performed once.
     */
    assertEquals(2, composeRule.activity.supportFragmentManager.fragments.size)
  }
}
