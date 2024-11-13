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

package br.com.orcinus.orca.app.demo

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import assertk.assertThat
import br.com.orcinus.orca.app.R
import br.com.orcinus.orca.app.demo.activity.DemoOrcaActivity
import br.com.orcinus.orca.feature.feed.FeedFragment
import br.com.orcinus.orca.feature.profiledetails.ProfileDetailsFragment
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class BottomNavigationTests {
  private inline val fragments
    get() = composeRule.activity.supportFragmentManager.fragments as List<Fragment>

  @get:Rule val composeRule = createAndroidComposeRule<DemoOrcaActivity>()

  @Test
  fun navigatesOnceWhenClickingFeedItemMultipleTimes() {
    repeat(2) { onView(withId(R.id.feed)).perform(click()) }
    assertThat(fragments).containsExactlyInstancesOf(FeedFragment::class)
  }

  @Test
  fun navigatesOnceWhenClickingProfileDetailsItemMultipleTimes() {
    repeat(2) { onView(withId(R.id.profile_details)).perform(click()) }
    assertThat(fragments)
      .containsExactlyInstancesOf(FeedFragment::class, ProfileDetailsFragment::class)
  }
}
