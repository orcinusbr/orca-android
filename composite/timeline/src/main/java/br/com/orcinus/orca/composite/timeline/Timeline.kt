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

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.composite.timeline.post.PostPreview
import br.com.orcinus.orca.composite.timeline.post.time.RelativeTimeProvider
import br.com.orcinus.orca.composite.timeline.post.time.rememberRelativeTimeProvider
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.snack.presenter.ErrorPresentation
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.snack.presenter.SnackbarPresenter
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.snack.presenter.rememberSnackbarPresenter
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.text.AutoSizeText
import br.com.orcinus.orca.platform.autos.overlays.refresh.Refresh
import br.com.orcinus.orca.platform.autos.overlays.refresh.Refreshable
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview
import br.com.orcinus.orca.platform.core.image.sample
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader
import com.jeanbarrossilva.loadable.list.ListLoadable
import java.net.URI

/** Tag that identifies an [EmptyTimelineMessage] for testing purposes. */
internal const val EmptyTimelineMessageTag = "empty-timeline-message"

/** Tag that identifies a [Timeline] for testing purposes. */
@InternalTimelineApi const val TimelineTag = "timeline"

/** [Timeline] content types for Compose to reuse the [Composable]s while lazily displaying them. */
private enum class TimelineContentType {
  /** Content type of a header. */
  Header,

  /** Content type of [PostPreview]s. */
  PostPreview,

  /** Content type of a [br.com.orcinus.orca.composite.timeline.RenderEffect]. */
  RenderEffect
}

/** Default values of a [Timeline]. */
@Immutable
object TimelineDefaults {
  /** Index from which pagination starts in a [Timeline]. */
  @Suppress("ConstPropertyName") const val InitialSubsequentPaginationIndex = 1
}

/**
 * Displays [PostPreview]s in a paginated way.
 *
 * @param postPreviewsLoadable [ListLoadable] of [PostPreview]s to be lazily shown.
 * @param onFavorite Callback run whenever the [PostPreview] associated to the given ID requests the
 *   [Post] to have its "favorited" state toggled.
 * @param onRepost Callback run whenever the [PostPreview] associated to the given ID requests the
 *   [Post] to have its "reblogged" state toggled.
 * @param onShare Callback run whenever a [PostPreview] requests the [Post]'s [URI] is requested to
 *   be shared.
 * @param onClick Callback run whenever the [PostPreview] associated to the given ID is clicked.
 * @param onNext Operation to be performed whenever pagination is requested. Provided index starts
 *   at one, mainly because it is implied that the current page is 0 if the content has already been
 *   loaded.
 * @param modifier [Modifier] to be applied to the underlying [Composable].
 * @param state [LazyStaggeredGridState] through which scroll will be observed.
 * @param contentPadding [PaddingValues] to pad the content with.
 * @param refresh Configuration for the swipe-to-refresh behavior to be adopted.
 * @param relativeTimeProvider [RelativeTimeProvider] that provides the time that's passed since a
 *   [Post] was published.
 * @param snackbarPresenter [SnackbarPresenter] by which [Snackbar]s informing of load errors are
 *   presented.
 * @param header [Composable] to be shown above the [PostPreview]s.
 */
@Composable
fun Timeline(
  postPreviewsLoadable: ListLoadable<PostPreview>,
  onFavorite: (id: String) -> Unit,
  onRepost: (id: String) -> Unit,
  onShare: (URI) -> Unit,
  onClick: (id: String) -> Unit,
  onNext: (index: Int) -> Unit,
  modifier: Modifier = Modifier,
  state: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
  contentPadding: PaddingValues = PaddingValues(),
  refresh: Refresh = Refresh.Disabled,
  relativeTimeProvider: RelativeTimeProvider = rememberRelativeTimeProvider(),
  snackbarPresenter: SnackbarPresenter = rememberSnackbarPresenter(),
  header: @Composable (() -> Unit)? = null
) {
  when (postPreviewsLoadable) {
    is ListLoadable.Empty ->
      EmptyTimelineMessage(onNext, contentPadding, modifier, header?.let { { it() } })
    is ListLoadable.Loading ->
      LoadingTimeline(onNext, modifier, contentPadding, header?.let { { it() } })
    is ListLoadable.Populated ->
      LoadedTimeline(
        postPreviewsLoadable.content,
        onFavorite,
        onRepost,
        onShare,
        onClick,
        onNext,
        modifier,
        state,
        contentPadding,
        refresh,
        relativeTimeProvider,
        header
      )
    is ListLoadable.Failed ->
      ErrorPresentation(
        postPreviewsLoadable.error,
        refreshListener = { onNext(TimelineDefaults.InitialSubsequentPaginationIndex) },
        snackbarPresenter,
        modifier,
        state
      )
  }
}

/**
 * [LazyColumn] for displaying loading [PostPreview]s.
 *
 * @param onNext Operation to be performed whenever pagination is requested. Provided index starts
 *   at one, mainly because it is implied that the current page is 0 if the content has already been
 *   loaded.
 * @param modifier [Modifier] to be applied to the underlying [LazyColumn].
 * @param contentPadding [PaddingValues] to pad the content with.
 * @param header [Composable] to be shown above the [PostPreview]s.
 */
@Composable
fun LoadingTimeline(
  onNext: (index: Int) -> Unit,
  modifier: Modifier = Modifier,
  contentPadding: PaddingValues = PaddingValues(),
  header: @Composable (LazyStaggeredGridItemScope.() -> Unit)? = null
) {
  Timeline(
    onNext = {},
    modifier,
    header,
    contentPadding = contentPadding,
    refresh = Refresh.immediate { onNext(TimelineDefaults.InitialSubsequentPaginationIndex) }
  ) {
    items(128) { PostPreview() }
  }
}

/**
 * [LazyColumn] for displaying paged, loaded [PostPreview]s.
 *
 * @param postPreviews [PostPreview]s to be lazily shown.
 * @param onFavorite Callback run whenever the [PostPreview] associated to the given ID requests the
 *   [Post] to have its "favorited" state toggled.
 * @param onRepost Callback run whenever the [PostPreview] associated to the given ID requests the
 *   [Post] to have its "reblogged" state toggled.
 * @param onShare Callback run whenever a [PostPreview] requests the [Post]'s [URI] is requested to
 *   be shared.
 * @param onClick Callback run whenever the [PostPreview] associated to the given ID is clicked.
 * @param onNext Operation to be performed whenever pagination is requested. Provided index starts
 *   at one, mainly because it is implied that the current page is 0 if the content has already been
 *   loaded.
 * @param modifier [Modifier] to be applied to the underlying [LazyColumn].
 * @param state [LazyStaggeredGridState] through which scroll will be observed.
 * @param contentPadding [PaddingValues] to pad the content with.
 * @param refresh Configuration for the swipe-to-refresh behavior to be adopted.
 * @param relativeTimeProvider [RelativeTimeProvider] that provides the time that's passed since a
 *   [Post] was published.
 * @param header [Composable] to be shown above the [PostPreview]s.
 */
@Composable
fun LoadedTimeline(
  postPreviews: List<PostPreview>,
  onFavorite: (id: String) -> Unit,
  onRepost: (id: String) -> Unit,
  onShare: (URI) -> Unit,
  onClick: (id: String) -> Unit,
  onNext: (index: Int) -> Unit,
  modifier: Modifier = Modifier,
  state: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
  contentPadding: PaddingValues = PaddingValues(),
  refresh: Refresh = Refresh.Disabled,
  relativeTimeProvider: RelativeTimeProvider = rememberRelativeTimeProvider(),
  header: @Composable (() -> Unit)? = null
) {
  if (postPreviews.isEmpty()) {
    EmptyTimelineMessage(onNext, contentPadding, modifier, header?.let { { it() } })
  } else {
    Timeline(onNext, modifier, header?.let { { it() } }, state, contentPadding, refresh) {
      items(
        postPreviews,
        key = PostPreview::id,
        contentType = { TimelineContentType.PostPreview }
      ) {
        PostPreview(
          it,
          onFavorite = { onFavorite(it.id) },
          onRepost = { onRepost(it.id) },
          onShare = { onShare(it.uri) },
          onClick = { onClick(it.id) },
          relativeTimeProvider = relativeTimeProvider
        )
      }
    }
  }
}

/**
 * [LazyColumn] for displaying paged content.
 *
 * @param onNext Operation to be performed whenever pagination is requested. Provided index starts
 *   at one, mainly because it is implied that the current page is 0 if the content has already been
 *   loaded.
 * @param modifier [Modifier] to be applied to the underlying [LazyColumn].
 * @param header [Composable] to be shown above the [content].
 * @param state [LazyStaggeredGridState] through which scroll will be observed.
 * @param contentPadding [PaddingValues] to pad the contents ([header] + [content]) with.
 * @param refresh Configuration for the swipe-to-refresh behavior to be adopted.
 * @param content Content to be lazily shown below the [header].
 */
@Composable
fun Timeline(
  onNext: (index: Int) -> Unit,
  modifier: Modifier = Modifier,
  header: (@Composable LazyStaggeredGridItemScope.() -> Unit)? = null,
  state: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
  contentPadding: PaddingValues = PaddingValues(),
  refresh: Refresh = Refresh.Disabled,
  content: LazyStaggeredGridScope.() -> Unit
) {
  Refreshable(refresh) {
    val itemCount by remember {
      derivedStateOf(structuralEqualityPolicy()) { state.layoutInfo.totalItemsCount }
    }
    var itemCountPriorToLastPagination by remember { mutableStateOf<Int?>(null) }
    var index by remember { mutableIntStateOf(TimelineDefaults.InitialSubsequentPaginationIndex) }
    val paginate by rememberUpdatedState {
      itemCountPriorToLastPagination = itemCount
      onNext(index++)
    }
    val paginateOnChangedItemCount by rememberUpdatedState {
      if (itemCount != itemCountPriorToLastPagination) {
        paginate()
      }
    }

    LazyVerticalStaggeredGrid(
      StaggeredGridCells.Adaptive(minSize = 300.dp),
      modifier.testTag(TimelineTag),
      state,
      contentPadding
    ) {
      header?.let { item(contentType = TimelineContentType.Header, content = it) }
      content()
      renderEffect(
        TimelineContentType.RenderEffect,
        content,
        itemCount,
        effect = paginateOnChangedItemCount
      )
    }
  }
}

/**
 * Displays [PostPreview]s in a paginated way.
 *
 * This overload is stateless by default and is intended for previewing and testing purposes only.
 *
 * @param postPreviewsLoadable [ListLoadable] of [PostPreview]s to be lazily shown.
 * @param modifier [Modifier] to be applied to the underlying [LazyColumn].
 * @param snackbarPresenter [SnackbarPresenter] by which [Snackbar]s informing of load errors are
 *   presented.
 * @param header [Composable] to be shown above the [PostPreview]s.
 */
@Composable
@VisibleForTesting
internal fun LoadedTimeline(
  postPreviewsLoadable: ListLoadable<PostPreview>,
  modifier: Modifier = Modifier,
  snackbarPresenter: SnackbarPresenter = rememberSnackbarPresenter(),
  header: @Composable (() -> Unit)? = null
) {
  Timeline(
    postPreviewsLoadable,
    onFavorite = {},
    onRepost = {},
    onShare = {},
    onClick = {},
    onNext = {},
    modifier,
    snackbarPresenter = snackbarPresenter,
    header = header
  )
}

/**
 * [LazyColumn] for displaying paged, loaded [PostPreview]s.
 *
 * This overload is stateless by default and is intended for previewing and testing purposes only.
 *
 * @param postPreviews [PostPreview]s to be lazily shown.
 * @param modifier [Modifier] to be applied to the underlying [LazyColumn].
 * @param onNext Callback run whenever the bottom is being reached.
 * @param header [Composable] to be shown above the [PostPreview]s.
 * @see PostPreview.createSamples
 */
@Composable
@VisibleForTesting
internal fun LoadedTimeline(
  postPreviews: List<PostPreview>,
  modifier: Modifier = Modifier,
  onNext: (index: Int) -> Unit = {},
  header: @Composable (() -> Unit)? = null
) {
  LoadedTimeline(
    postPreviews,
    onFavorite = {},
    onRepost = {},
    onShare = {},
    onClick = {},
    onNext,
    modifier,
    header = header
  )
}

/**
 * Message stating that the [Timeline] is empty.
 *
 * @param onNext Operation to be performed whenever pagination is requested. Provided index starts
 *   at one, mainly because it is implied that the current page is 0 if the content has already been
 *   loaded.
 * @param header [Composable] to be shown above the message.
 * @param contentPadding [PaddingValues] to pad the contents with.
 * @param modifier [Modifier] to be applied to the underlying [Column].
 */
@Composable
private fun EmptyTimelineMessage(
  onNext: (index: Int) -> Unit,
  contentPadding: PaddingValues,
  modifier: Modifier = Modifier,
  header: @Composable (LazyItemScope.() -> Unit)?
) {
  val spacing = AutosTheme.spacings.large.dp

  Refreshable(Refresh.immediate { onNext(TimelineDefaults.InitialSubsequentPaginationIndex) }) {
    LazyColumn(
      modifier.testTag(EmptyTimelineMessageTag).fillMaxSize(),
      contentPadding = contentPadding,
      verticalArrangement = Arrangement.spacedBy(spacing),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      header?.let { item(content = it) }

      item {
        BoxWithConstraints {
          Column(
            Modifier.fillParentMaxSize().drawWithContent {
              translate(top = -(center.y / 2f + center.y - size.height / 2f)) {
                this@drawWithContent.drawContent()
              }
            },
            verticalArrangement = Arrangement.spacedBy(spacing, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(
              AutosTheme.iconography.empty.asImageVector,
              contentDescription = stringResource(R.string.composite_timeline_empty),
              Modifier.size(32.dp),
              tint = AutosTheme.colors.secondary.asColor
            )

            Text(
              stringResource(R.string.composite_timeline_empty_message),
              textAlign = TextAlign.Center,
              style = AutosTheme.typography.headlineMedium
            )
          }
        }
      }
    }
  }
}

/** Preview of a loading [Timeline]. */
@Composable
@MultiThemePreview
private fun LoadingTimelinePreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) { LoadingTimeline() }
  }
}

/** Preview of an empty [Timeline]. */
@Composable
@MultiThemePreview
private fun EmptyTimelinePreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) {
      LoadedTimeline(ListLoadable.Empty())
    }
  }
}

/** Preview of an empty [Timeline] with a header. */
@Composable
@MultiThemePreview
private fun EmptyTimelineWithHeaderPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) {
      LoadedTimeline(emptyList()) {
        AutoSizeText(
          "Header",
          Modifier.padding(
            start = AutosTheme.spacings.large.dp,
            top = AutosTheme.spacings.large.dp,
            end = AutosTheme.spacings.large.dp,
            bottom = AutosTheme.spacings.medium.dp
          ),
          style = AutosTheme.typography.headlineLarge
        )
      }
    }
  }
}

/** Preview of a populated [Timeline]. */
@Composable
@MultiThemePreview
private fun PopulatedTimelinePreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) {
      LoadedTimeline(
        PostPreview.createSamples(
          SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
            .withDefaultProfiles()
            .withDefaultPosts()
            .build()
            .postProvider,
          AutosTheme.colors
        )
      )
    }
  }
}

/** Preview of a populated [Timeline] with a header. */
@Composable
@MultiThemePreview
private fun PopulatedTimelineWithHeaderPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) {
      LoadingTimeline {
        AutoSizeText(
          "Header",
          Modifier.padding(
            start = AutosTheme.spacings.large.dp,
            top = AutosTheme.spacings.large.dp,
            end = AutosTheme.spacings.large.dp,
            bottom = AutosTheme.spacings.medium.dp
          ),
          style = AutosTheme.typography.headlineLarge
        )
      }
    }
  }
}

/**
 * [LazyColumn] for displaying loading [PostPreview]s.
 *
 * @param modifier [Modifier] to be applied to the underlying [LazyColumn].
 * @param header [Composable] to be shown above the [PostPreview]s.
 */
@Composable
private fun LoadingTimeline(
  modifier: Modifier = Modifier,
  header: (@Composable LazyStaggeredGridItemScope.() -> Unit)? = null
) {
  LoadingTimeline(onNext = {}, modifier, header = header)
}
