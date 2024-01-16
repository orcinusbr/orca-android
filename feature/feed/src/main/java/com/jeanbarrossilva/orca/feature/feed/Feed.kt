/*
 * Copyright © 2023-2024 Orca
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

package com.jeanbarrossilva.orca.feature.feed

import androidx.annotation.VisibleForTesting
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.loadable.list.toListLoadable
import com.jeanbarrossilva.loadable.list.toSerializableList
import com.jeanbarrossilva.orca.composite.timeline.Timeline
import com.jeanbarrossilva.orca.composite.timeline.post.PostPreview
import com.jeanbarrossilva.orca.composite.timeline.refresh.Refresh
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.icon.HoverableIconButton
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.Scaffold
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.TopAppBar
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.TopAppBarDefaults
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.text.AutoSizeText
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.plus
import com.jeanbarrossilva.orca.platform.autos.overlays.asPaddingValues
import com.jeanbarrossilva.orca.platform.autos.reactivity.BottomAreaAvailabilityNestedScrollConnection
import com.jeanbarrossilva.orca.platform.autos.reactivity.OnBottomAreaAvailabilityChangeListener
import com.jeanbarrossilva.orca.platform.autos.reactivity.rememberBottomAreaAvailabilityNestedScrollConnection
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import java.net.URL

const val FEED_SEARCH_ACTION_TAG = "feed-search-action-tag"
const val FEED_FLOATING_ACTION_BUTTON_TAG = "feed-floating-action-button"

@Composable
@VisibleForTesting
fun Feed(modifier: Modifier = Modifier) {
  Feed(PostPreview.samples.toSerializableList().toListLoadable(), modifier)
}

@Composable
internal fun Feed(
  viewModel: FeedViewModel,
  boundary: FeedBoundary,
  onBottomAreaAvailabilityChangeListener: OnBottomAreaAvailabilityChangeListener,
  modifier: Modifier = Modifier
) {
  var isTimelineRefreshing by remember { mutableStateOf(false) }
  val postPreviewsLoadable by
    viewModel.postPreviewsLoadableFlow.collectAsState(ListLoadable.Loading())
  val bottomAreaAvailabilityNestedScrollConnection =
    rememberBottomAreaAvailabilityNestedScrollConnection(onBottomAreaAvailabilityChangeListener)

  Feed(
    postPreviewsLoadable,
    onSearch = boundary::navigateToSearch,
    isTimelineRefreshing,
    onTimelineRefresh = {
      isTimelineRefreshing = true
      viewModel.requestRefresh { isTimelineRefreshing = false }
    },
    onFavorite = viewModel::favorite,
    onRepost = viewModel::repost,
    onShare = viewModel::share,
    onPostClick = boundary::navigateToPostDetails,
    onNext = viewModel::loadPostsAt,
    onComposition = boundary::navigateToComposer,
    bottomAreaAvailabilityNestedScrollConnection,
    modifier
  )
}

@Composable
internal fun Feed(
  postPreviewsLoadable: ListLoadable<PostPreview>,
  modifier: Modifier = Modifier,
  onFavorite: (postID: String) -> Unit = {}
) {
  Feed(
    postPreviewsLoadable,
    onSearch = {},
    isTimelineRefreshing = false,
    onTimelineRefresh = {},
    onFavorite,
    onRepost = {},
    onShare = {},
    onPostClick = {},
    onNext = {},
    onComposition = {},
    BottomAreaAvailabilityNestedScrollConnection.empty,
    modifier
  )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Feed(
  postPreviewsLoadable: ListLoadable<PostPreview>,
  onSearch: () -> Unit,
  isTimelineRefreshing: Boolean,
  onTimelineRefresh: () -> Unit,
  onFavorite: (postID: String) -> Unit,
  onRepost: (postID: String) -> Unit,
  onShare: (URL) -> Unit,
  onPostClick: (postID: String) -> Unit,
  onNext: (index: Int) -> Unit,
  onComposition: () -> Unit,
  bottomAreaAvailabilityNestedScrollConnection: BottomAreaAvailabilityNestedScrollConnection,
  modifier: Modifier = Modifier
) {
  val topAppBarScrollBehavior = TopAppBarDefaults.scrollBehavior

  Scaffold(
    modifier,
    topAppBar = {
      @OptIn(ExperimentalMaterial3Api::class)
      TopAppBar(
        title = { AutoSizeText(stringResource(R.string.feature_feed)) },
        actions = {
          HoverableIconButton(onClick = onSearch, Modifier.testTag(FEED_SEARCH_ACTION_TAG)) {
            Icon(
              AutosTheme.iconography.search.asImageVector,
              contentDescription = stringResource(R.string.feature_feed_search)
            )
          }
        },
        scrollBehavior = topAppBarScrollBehavior
      )
    },
    floatingActionButton = {
      if (postPreviewsLoadable.isLoaded) {
        FloatingActionButton(
          onClick = onComposition,
          Modifier.testTag(FEED_FLOATING_ACTION_BUTTON_TAG)
        ) {
          Icon(
            AutosTheme.iconography.compose.filled.asImageVector,
            contentDescription = stringResource(R.string.feature_feed_compose)
          )
        }
      }
    }
  ) {
    Timeline(
      postPreviewsLoadable,
      onFavorite,
      onRepost,
      onShare,
      onPostClick,
      onNext,
      Modifier.nestedScroll(bottomAreaAvailabilityNestedScrollConnection)
        .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
      contentPadding = it + AutosTheme.overlays.fab.asPaddingValues,
      refresh =
        Refresh(isTimelineRefreshing, indicatorOffset = it.calculateTopPadding(), onTimelineRefresh)
    )
  }
}

@Composable
@MultiThemePreview
private fun LoadingFeedPreview() {
  AutosTheme { Feed(ListLoadable.Loading()) }
}

@Composable
@MultiThemePreview
private fun EmptyFeedPreview() {
  AutosTheme { Feed(ListLoadable.Empty()) }
}

@Composable
@MultiThemePreview
private fun PopulatedFeedPreview() {
  AutosTheme { Feed(PostPreview.samples.toSerializableList().toListLoadable()) }
}
