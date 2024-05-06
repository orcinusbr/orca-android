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

package br.com.orcinus.orca.platform.autos.kit.scaffold.bar.navigation.view

import android.content.res.ColorStateList
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.viewinterop.AndroidView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import assertk.assertThat
import assertk.assertions.isTrue
import br.com.orcinus.orca.platform.autos.R
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.navigation.NavigationBarDefaults
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.navigation.view.image.withImageTint
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.navigation.view.text.hasTextColors
import br.com.orcinus.orca.platform.autos.test.kit.scaffold.bar.navigation.onTab
import br.com.orcinus.orca.platform.autos.test.kit.scaffold.bar.navigation.view.image.withDrawable
import br.com.orcinus.orca.platform.autos.test.theme.require
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.testing.context
import br.com.orcinus.orca.platform.testing.emptyStringResourceID
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class NavigationBarViewTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun composes() {
    var hasComposed = false
    composeRule.setContent {
      CompositionLocalProvider(LocalInspectionMode provides true) {
        AndroidView(::NavigationBarView) {
          it.setOnCompositionListener {
            hasComposed = true
            it.setOnCompositionListener(null)
          }
        }
      }
    }
    assertThat(hasComposed).isTrue()
  }

  @Test
  fun isThemed() {
    var hasBeenThemed = false
    composeRule.setContent {
      AndroidView(::NavigationBarView) {
        it.setOnCompositionListener {
          AutosTheme.require()
          hasBeenThemed = true
          it.setOnCompositionListener(null)
        }
      }
    }
    assertThat(hasBeenThemed).isTrue()
  }

  @Test
  fun identifiesTitle() {
    val view = NavigationBarView(context)
    composeRule.setContent { AndroidView({ view }) { it.setTitle(":P") } }
    onView(withId(view.titleViewID)).check(matches(isCompletelyDisplayed()))
  }

  @Test
  fun titleIsColoredWithCurrentContentColor() {
    val view = NavigationBarView(context)
    composeRule.setContent { AndroidView({ view }) }
    onView(withId(view.titleViewID))
      .check(
        matches(hasTextColors(ColorStateList.valueOf(NavigationBarDefaults.ContentColor.toArgb())))
      )
  }

  @Test
  fun setsTitle() {
    val view = NavigationBarView(context)
    composeRule.setContent { AndroidView({ view }) { it.setTitle(":)") } }
    onView(withId(view.titleViewID)).check(matches(isDisplayed()))
  }

  @Test(expected = NoMatchingViewException::class)
  fun doesNotIdentifyActionButtonWhenItHasNotBeenSet() {
    val view = NavigationBarView(context)
    composeRule.setContent { AndroidView({ view }) }
    onView(withId(view.actionButtonID)).check(matches(isCompletelyDisplayed()))
  }

  @Test
  fun identifiesActionButtonWhenItHasBeenSet() {
    val view =
      NavigationBarView(context).apply { setAction(R.drawable.icon_back, emptyStringResourceID) {} }
    composeRule.setContent { AndroidView({ view }) }
    onView(withId(view.actionButtonID)).check(matches(isCompletelyDisplayed()))
  }

  @Test
  fun tintsActionIconWithCurrentContentColor() {
    val view = NavigationBarView(context)
    composeRule.setContent {
      AndroidView({ view }) { it.setAction(R.drawable.icon_back, emptyStringResourceID) {} }
    }
    onView(withId(view.actionButtonID))
      .check(
        matches(withImageTint(ColorStateList.valueOf(NavigationBarDefaults.ContentColor.toArgb())))
      )
  }

  @Test
  fun setsActionIcon() {
    val view = NavigationBarView(context)
    composeRule.setContent {
      AndroidView({ view }) { it.setAction(R.drawable.icon_back, emptyStringResourceID) {} }
    }
    onView(withId(view.actionButtonID))
      .check(
        matches(
          withDrawable(context, R.drawable.icon_back) {
            it.setTint(NavigationBarDefaults.ContentColor.toArgb())
          }
        )
      )
  }

  @Test
  fun describesAction() {
    val view = NavigationBarView(context)
    composeRule.setContent {
      AndroidView({ view }) { it.setAction(R.drawable.icon_back, emptyStringResourceID) {} }
    }
    onView(withId(view.actionButtonID))
      .check(matches(withContentDescription(emptyStringResourceID)))
  }

  @Test
  fun clicksActionButton() {
    val view = NavigationBarView(context)
    var hasActionButtonBeenClicked = false
    composeRule.setContent {
      AndroidView({ view }) {
        it.setAction(R.drawable.icon_back, emptyStringResourceID) {
          hasActionButtonBeenClicked = true
        }
      }
    }
    onView(withId(view.actionButtonID)).perform(click())
    assertThat(hasActionButtonBeenClicked).isTrue()
  }

  @Test
  fun setsOnActionButtonClickListener() {
    val view = NavigationBarView(context)
    var hasActionButtonBeenClicked = false
    composeRule.setContent {
      AndroidView({ view }) {
        it.setAction(R.drawable.icon_back, emptyStringResourceID) {}
        it.setOnActionButtonClickListener { hasActionButtonBeenClicked = true }
      }
    }
    onView(withId(view.actionButtonID)).perform(click())
    assertThat(hasActionButtonBeenClicked).isTrue()
  }

  @Test
  fun addsTab() {
    composeRule
      .apply {
        setContent {
          AndroidView(::NavigationBarView) {
            it.addTab(android.R.id.tabs, R.drawable.icon_home_outlined, emptyStringResourceID) {
              false
            }
          }
        }
      }
      .onTab()
      .assertIsDisplayed()
  }

  @Test
  fun setsOnTabClickListener() {
    var hasTabBeenClicked = false
    composeRule
      .apply {
        setContent {
          AndroidView(::NavigationBarView) {
            it.addTab(android.R.id.tabs, R.drawable.icon_home_outlined, emptyStringResourceID) {
              false
            }
            it.setOnTabClickListener(android.R.id.tabs) {
              hasTabBeenClicked = true
              true
            }
          }
        }
      }
      .onTab()
      .performClick()
    assertThat(hasTabBeenClicked).isTrue()
  }

  @Test
  fun selectsTab() {
    var hasBeenSelected = false
    composeRule
      .apply {
        setContent {
          AndroidView(::NavigationBarView) {
            it.addTab(android.R.id.tabs, R.drawable.icon_home_outlined, emptyStringResourceID) {
              hasBeenSelected = true
              true
            }
          }
        }
      }
      .onTab()
      .performClick()
    assertThat(hasBeenSelected).isTrue()
  }

  @Test
  fun setsCurrentTab() {
    var hasBeenSet = false
    composeRule.setContent {
      AndroidView(::NavigationBarView) {
        it.addTab(android.R.id.tabs, R.drawable.icon_home_outlined, emptyStringResourceID) {
          hasBeenSet = true
          true
        }
        it.setCurrentTab(android.R.id.tabs)
      }
    }
    assertThat(hasBeenSet).isTrue()
  }
}
