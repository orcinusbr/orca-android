/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.app.demo

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.jeanbarrossilva.orca.app.R
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
