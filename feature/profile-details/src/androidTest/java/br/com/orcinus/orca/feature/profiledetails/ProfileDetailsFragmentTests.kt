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

package br.com.orcinus.orca.feature.profiledetails

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.sample.test.instance.SampleInstanceTestRule
import br.com.orcinus.orca.feature.profiledetails.navigation.BackwardsNavigationState
import br.com.orcinus.orca.feature.profiledetails.test.TestProfileDetailsModule
import br.com.orcinus.orca.platform.core.sample
import br.com.orcinus.orca.platform.navigation.BackStack
import br.com.orcinus.orca.platform.navigation.test.fragment.launchFragmentInNavigationContainer
import br.com.orcinus.orca.platform.testing.DefaultTimeout
import br.com.orcinus.orca.std.injector.test.InjectorTestRule
import com.jeanbarrossilva.loadable.placeholder.test.isLoading
import kotlin.test.Test
import org.junit.Rule
import org.junit.rules.RuleChain

internal class ProfileDetailsFragmentTests {
  private val injectorRule = InjectorTestRule {
    register<ProfileDetailsModule>(TestProfileDetailsModule)
  }
  private val sampleInstanceRule = SampleInstanceTestRule(Instance.sample)
  private val composeRule = createEmptyComposeRule()

  @get:Rule
  val ruleChain: RuleChain =
    RuleChain.outerRule(injectorRule).around(sampleInstanceRule).around(composeRule)

  @Test
  fun loadsPosts() {
    launchFragmentInNavigationContainer {
        ProfileDetailsFragment(
          BackStack.named(ProfileDetailsFragment::class.java.name),
          BackwardsNavigationState.Unavailable,
          Profile.sample.id
        )
      }
      .use {
        @OptIn(ExperimentalTestApi::class)
        composeRule.waitUntilDoesNotExist(
          isLoading(),
          timeoutMillis = DefaultTimeout.inWholeMilliseconds
        )
      }
  }
}
