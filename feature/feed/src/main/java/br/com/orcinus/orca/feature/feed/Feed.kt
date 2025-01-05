/*
 * Copyright © 2023–2025 Orcinus
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

package br.com.orcinus.orca.feature.feed

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
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
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.composite.timeline.Timeline
import br.com.orcinus.orca.composite.timeline.post.PostPreview
import br.com.orcinus.orca.composite.timeline.search.Searchable
import br.com.orcinus.orca.composite.timeline.search.top
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearchResult
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.kit.action.button.icon.HoverableIconButton
import br.com.orcinus.orca.platform.autos.kit.input.text.SearchTextFieldDefaults
import br.com.orcinus.orca.platform.autos.kit.scaffold.Scaffold
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.snack.presenter.rememberSnackbarPresenter
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.TopAppBar
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.TopAppBarDefaults
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.`if`
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.text.AutoSizeText
import br.com.orcinus.orca.platform.autos.overlays.refresh.Refresh
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview
import br.com.orcinus.orca.platform.core.image.sample
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.loadable.list.toListLoadable
import com.jeanbarrossilva.loadable.list.toSerializableList
import java.net.URI

const val FeedSearchActionTag = "feed-search-action"
const val FeedFloatingActionButtonTag = "feed-floating-action-button"

@Composable
@VisibleForTesting
fun Feed(modifier: Modifier = Modifier) {
  Feed(
    PostPreview.createSamples(
        SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
          .withDefaultProfiles()
          .withDefaultPosts()
          .build()
          .postProvider,
        AutosTheme.colors
      )
      .toSerializableList()
      .toListLoadable(),
    modifier
  )
}

@Composable
internal fun Feed(
  viewModel: FeedViewModel,
  onPostClick: (postID: String) -> Unit,
  onComposition: () -> Unit,
  modifier: Modifier = Modifier
) {
  val searchQuery by viewModel.searchQueryFlow.collectAsState()
  val searchResultsLoadable by
    viewModel.searchResultsLoadableFlow.collectAsState(ListLoadable.Loading())
  var isTimelineRefreshing by remember { mutableStateOf(false) }
  val postPreviewsLoadable by
    viewModel.postPreviewsLoadableFlow.collectAsState(ListLoadable.Loading())

  Feed(
    searchQuery,
    onSearchQueryChange = viewModel::search,
    searchResultsLoadable,
    postPreviewsLoadable,
    isTimelineRefreshing,
    onTimelineRefresh = {
      isTimelineRefreshing = true
      viewModel.requestRefresh { isTimelineRefreshing = false }
    },
    onFavorite = viewModel::favorite,
    onRepost = viewModel::repost,
    onShare = viewModel::share,
    onPostClick,
    onNext = viewModel::loadPostsAt,
    onComposition,
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
    searchQuery = "",
    onSearchQueryChange = {},
    searchResultsLoadable = ListLoadable.Empty(),
    postPreviewsLoadable,
    isTimelineRefreshing = false,
    onTimelineRefresh = {},
    onFavorite,
    onRepost = {},
    onShare = {},
    onPostClick = {},
    onNext = {},
    onComposition = {},
    modifier
  )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Feed(
  searchQuery: String,
  onSearchQueryChange: (searchQuery: String) -> Unit,
  searchResultsLoadable: ListLoadable<ProfileSearchResult>,
  postPreviewsLoadable: ListLoadable<PostPreview>,
  isTimelineRefreshing: Boolean,
  onTimelineRefresh: () -> Unit,
  onFavorite: (postID: String) -> Unit,
  onRepost: (postID: String) -> Unit,
  onShare: (URI) -> Unit,
  onPostClick: (postID: String) -> Unit,
  onNext: (index: Int) -> Unit,
  onComposition: () -> Unit,
  modifier: Modifier = Modifier
) {
  Searchable(
    TopAppBarDefaults.idleContainerColor,
    modifier,
    searchTextFieldOffset =
      DpOffset(x = 0.dp, y = WindowInsets.systemBars.top + SearchTextFieldDefaults.spacing * 2),
    searchTextFieldPadding = PaddingValues(horizontal = SearchTextFieldDefaults.spacing)
  ) {
    val topAppBarScrollBehavior = TopAppBarDefaults.scrollBehavior
    val snackbarPresenter = rememberSnackbarPresenter()
    val contentBlurRadius by contentBlurRadiusAsState
    val timelineTopContentPadding by
      animateDpAsState(
        searchTextFieldHeight.`if`(isSearching) {
          this + WindowInsets.systemBars.top + SearchTextFieldDefaults.spacing * 2
        },
        replacementAnimationSpec(),
        label = "Timeline top content padding"
      )

    Scaffold(
      topAppBar = {
        Replaceable(searchQuery, onSearchQueryChange, searchResultsLoadable) {
          @OptIn(ExperimentalMaterial3Api::class)
          TopAppBar(
            title = { AutoSizeText(stringResource(R.string.feature_feed)) },
            actions = {
              HoverableIconButton(onClick = ::show, Modifier.testTag(FeedSearchActionTag)) {
                Icon(
                  AutosTheme.iconography.search.asImageVector,
                  contentDescription = stringResource(R.string.feature_feed_search)
                )
              }
            },
            scrollBehavior = topAppBarScrollBehavior
          )
        }
      },
      floatingActionButton = {
        if (postPreviewsLoadable.isLoaded) {
          FloatingActionButton(
            onClick = {
              if (!containsSearchResults) {
                onComposition()
              }
            },
            Modifier.blur(contentBlurRadius, BlurredEdgeTreatment.Unbounded)
              .testTag(FeedFloatingActionButtonTag)
          ) {
            Icon(
              AutosTheme.iconography.compose.filled.asImageVector,
              contentDescription = stringResource(R.string.feature_feed_compose)
            )
          }
        }
      },
      snackbarPresenter = snackbarPresenter
    ) {
      navigable(Modifier.blur(contentBlurRadius)) {
        Timeline(
          postPreviewsLoadable,
          onFavorite,
          onRepost,
          onShare,
          onPostClick,
          onNext,
          Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
          contentPadding = PaddingValues(top = timelineTopContentPadding, bottom = 56.dp),
          refresh = Refresh(isTimelineRefreshing, onTimelineRefresh),
          snackbarPresenter = snackbarPresenter
        )
      }
    }
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
  AutosTheme {
    Feed(
      PostPreview.createSamples(
          SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
            .withDefaultProfiles()
            .withDefaultPosts()
            .build()
            .postProvider
        )
        .toSerializableList()
        .toListLoadable()
    )
  }
}
