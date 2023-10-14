package com.jeanbarrossilva.orca.feature.feed

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.loadable.list.toListLoadable
import com.jeanbarrossilva.loadable.list.toSerializableList
import com.jeanbarrossilva.orca.feature.feed.viewmodel.FeedViewModel
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.extensions.plus
import com.jeanbarrossilva.orca.platform.theme.kit.action.button.HoverableIconButton
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.Scaffold
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.top.TopAppBar
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.top.TopAppBarDefaults
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.top.text.AutoSizeText
import com.jeanbarrossilva.orca.platform.theme.reactivity.BottomAreaAvailabilityNestedScrollConnection
import com.jeanbarrossilva.orca.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import com.jeanbarrossilva.orca.platform.theme.reactivity.rememberBottomAreaAvailabilityNestedScrollConnection
import com.jeanbarrossilva.orca.platform.ui.component.timeline.Timeline
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TootPreview
import java.net.URL

const val FEED_FLOATING_ACTION_BUTTON_TAG = "feed-floating-action-button"

@Composable
internal fun Feed(
  viewModel: FeedViewModel,
  boundary: FeedBoundary,
  onBottomAreaAvailabilityChangeListener: OnBottomAreaAvailabilityChangeListener,
  modifier: Modifier = Modifier
) {
  val tootPreviewsLoadable by viewModel.tootPreviewsLoadableFlow.collectAsState()
  val bottomAreaAvailabilityNestedScrollConnection =
    rememberBottomAreaAvailabilityNestedScrollConnection(onBottomAreaAvailabilityChangeListener)

  DisposableEffect(tootPreviewsLoadable) {
    tootPreviewsLoadable.let {
      if (it is ListLoadable.Failed) {
        throw it.error
      }
    }
    onDispose { }
  }

  Feed(
    tootPreviewsLoadable,
    onSearch = boundary::navigateToSearch,
    onHighlightClick = boundary::navigateTo,
    onFavorite = viewModel::favorite,
    onReblog = viewModel::reblog,
    onShare = viewModel::share,
    onTootClick = boundary::navigateToTootDetails,
    onNext = viewModel::loadTootsAt,
    onComposition = boundary::navigateToComposer,
    bottomAreaAvailabilityNestedScrollConnection,
    modifier
  )
}

@Composable
internal fun Feed(
  tootPreviewsLoadable: ListLoadable<TootPreview>,
  modifier: Modifier = Modifier,
  onFavorite: (tootID: String) -> Unit = {}
) {
  Feed(
    tootPreviewsLoadable,
    onSearch = {},
    onHighlightClick = {},
    onFavorite,
    onReblog = {},
    onShare = {},
    onTootClick = {},
    onNext = {},
    onComposition = {},
    BottomAreaAvailabilityNestedScrollConnection.empty,
    modifier
  )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Feed(
  tootPreviewsLoadable: ListLoadable<TootPreview>,
  onSearch: () -> Unit,
  onHighlightClick: (URL) -> Unit,
  onFavorite: (tootID: String) -> Unit,
  onReblog: (tootID: String) -> Unit,
  onShare: (URL) -> Unit,
  onTootClick: (tootID: String) -> Unit,
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
        title = { AutoSizeText(stringResource(R.string.feed)) },
        actions = {
          HoverableIconButton(onClick = onSearch) {
            Icon(
              OrcaTheme.iconography.search,
              contentDescription = stringResource(R.string.feed_search)
            )
          }
        },
        scrollBehavior = topAppBarScrollBehavior
      )
    },
    floatingActionButton = {
      if (tootPreviewsLoadable.isLoaded) {
        FloatingActionButton(
          onClick = onComposition,
          Modifier.testTag(FEED_FLOATING_ACTION_BUTTON_TAG)
        ) {
          Icon(
            OrcaTheme.iconography.compose.filled,
            contentDescription = stringResource(R.string.feed_compose)
          )
        }
      }
    }
  ) {
    Timeline(
      tootPreviewsLoadable,
      onHighlightClick,
      onFavorite,
      onReblog,
      onShare,
      onTootClick,
      onNext,
      Modifier.nestedScroll(bottomAreaAvailabilityNestedScrollConnection)
        .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
      contentPadding = it + OrcaTheme.overlays.fab
    )
  }
}

@Composable
@MultiThemePreview
private fun LoadingFeedPreview() {
  OrcaTheme { Feed(ListLoadable.Loading()) }
}

@Composable
@MultiThemePreview
private fun EmptyFeedPreview() {
  OrcaTheme { Feed(ListLoadable.Empty()) }
}

@Composable
@MultiThemePreview
private fun PopulatedFeedPreview() {
  OrcaTheme { Feed(TootPreview.samples.toSerializableList().toListLoadable()) }
}
