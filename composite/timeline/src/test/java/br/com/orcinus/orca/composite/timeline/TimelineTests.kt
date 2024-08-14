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

package br.com.orcinus.orca.composite.timeline

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.TouchInjectionScope
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.composite.timeline.test.onTimeline
import br.com.orcinus.orca.composite.timeline.test.performScrollToBottom
import br.com.orcinus.orca.platform.autos.i18n.ReadableThrowable
import br.com.orcinus.orca.platform.autos.kit.scaffold.Scaffold
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.snack.presenter.rememberSnackbarPresenter
import br.com.orcinus.orca.platform.autos.overlays.refresh.Refresh
import br.com.orcinus.orca.platform.autos.test.kit.scaffold.bar.snack.onSnackbar
import br.com.orcinus.orca.platform.autos.test.overlays.refresh.onRefreshIndicator
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.loadable.list.ListLoadable
import kotlin.test.Test
import kotlin.time.Duration.Companion.days
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class TimelineTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun showsEmptyMessageWhenListLoadableIsEmpty() {
    composeRule.setContent { AutosTheme { LoadedTimeline(ListLoadable.Empty()) } }
    composeRule.onNodeWithTag(EmptyTimelineMessageTag).assertIsDisplayed()
  }

  @Test
  fun showsRefreshIndicatorForeverWhenRefreshIsIndefinite() {
    composeRule.setContent { Timeline(onNext = {}, refresh = Refresh.Indefinite) {} }
    composeRule.mainClock.advanceTimeBy(256.days.inWholeMilliseconds)
    composeRule.onRefreshIndicator().assertIsDisplayed()
  }

  @Test
  fun providesNextIndexWhenReachingBottom() {
    val indices = mutableStateListOf<Int>()
    composeRule.setContent {
      Timeline(
        onNext = {
          if (indices.size < 2) {
            indices += it
          }
        }
      ) {
        items(indices) { Spacer(Modifier.fillMaxWidth()) }
      }
    }
    composeRule.onTimeline().performScrollToBottom().performScrollToBottom()
    assertThat(indices).containsExactly(1, 2)
  }

  @Test
  fun doesNotProvideNextIndexWhenReachingBottomMultipleTimesAndNoItemsHaveBeenAdded() {
    val indices = hashSetOf<Int>()
    composeRule
      .apply {
        setContent {
          Timeline(onNext = { indices += it }) { item { Spacer(Modifier.fillMaxWidth()) } }
        }
      }
      .onTimeline()
      .performScrollToBottom()
      .performTouchInput(TouchInjectionScope::swipeDown)
      .performScrollToBottom()
    assertThat(indices).isEqualTo(hashSetOf(1))
  }

  @Test(UnsupportedOperationException::class)
  fun throwsWhenExceptionCausedByANonReadableThrowableIsThrown() {
    composeRule.apply {
      setContent {
        AutosTheme {
          val snackbarPresenter = rememberSnackbarPresenter()

          Scaffold(snackbarPresenter = snackbarPresenter) {
            expanded {
              LoadedTimeline(
                ListLoadable.Failed(UnsupportedOperationException(Exception())),
                snackbarPresenter = snackbarPresenter
              )
            }
          }
        }
      }
    }
  }

  @Test
  fun showsSnackbarWhenExceptionCausedByAReadableThrowableIsThrown() {
    composeRule
      .apply {
        setContent {
          AutosTheme {
            val snackbarPresenter = rememberSnackbarPresenter()

            Scaffold(snackbarPresenter = snackbarPresenter) {
              expanded {
                LoadedTimeline(
                  ListLoadable.Failed(Exception(ReadableThrowable.default)),
                  snackbarPresenter = snackbarPresenter
                )
              }
            }
          }
        }
      }
      .onSnackbar()
      .assertIsDisplayed()
  }
}
