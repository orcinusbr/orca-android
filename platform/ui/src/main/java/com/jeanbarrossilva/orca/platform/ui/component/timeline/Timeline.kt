package com.jeanbarrossilva.orca.platform.ui.component.timeline

import androidx.annotation.RestrictTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.text.AutoSizeText
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.ui.R
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.PostPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.time.RelativeTimeProvider
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.time.rememberRelativeTimeProvider
import com.jeanbarrossilva.orca.platform.ui.component.timeline.refresh.Refresh
import com.jeanbarrossilva.orca.platform.ui.component.timeline.refresh.isInProgress
import java.net.URL

/** Tag that identifies an [EmptyTimelineMessage] for testing purposes. */
internal const val EMPTY_TIMELINE_MESSAGE_TAG = "empty-timeline-tag"

/** Tag that identifies dividers between [PostPreview]s in a [Timeline] for testing purposes. */
internal const val TIMELINE_DIVIDER_TAG = "timeline-divider"

/** Tag that identifies a [Timeline]'s refresh indicator for testing purposes. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
const val TIMELINE_REFRESH_INDICATOR = "timeline-refresh-indicator"

/** Tag that identifies a [Timeline] for testing purposes. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX) const val TIMELINE_TAG = "timeline"

/** [Timeline] content types for Compose to reuse the [Composable]s while lazily displaying them. */
private enum class TimelineContentType {
  /** Content type for the header. */
  HEADER,

  /** Content type for [PostPreview]s. */
  POST_PREVIEW,

  /**
   * Content type for the [com.jeanbarrossilva.orca.platform.ui.component.timeline.RenderEffect].
   */
  RENDER_EFFECT
}

/**
 * Displays [PostPreview]s in a paginated way.
 *
 * @param postPreviewsLoadable [ListLoadable] of [PostPreview]s to be lazily shown.
 * @param onHighlightClick Callback run whenever the [PostPreview]'s [PostPreview.highlight] is
 *   clicked.
 * @param onFavorite Callback run whenever the [PostPreview] associated to the given ID requests the
 *   [Post] to have its "favorited" state toggled.
 * @param onRepost Callback run whenever the [PostPreview] associated to the given ID requests the
 *   [Post] to have its "reblogged" state toggled.
 * @param onShare Callback run whenever a [PostPreview] requests the [Post]'s [URL] is requested to
 *   be shared.
 * @param onClick Callback run whenever the [PostPreview] associated to the given ID is clicked.
 * @param onNext Callback run whenever the bottom is being reached.
 * @param modifier [Modifier] to be applied to the underlying [LazyColumn].
 * @param state [LazyListState] through which scroll will be observed.
 * @param contentPadding [PaddingValues] to pad the content with.
 * @param refresh Configuration for the swipe-to-refresh behavior to be adopted.
 * @param relativeTimeProvider [RelativeTimeProvider] that provides the time that's passed since a
 *   [Post] was published.
 * @param header [Composable] to be shown above the [PostPreview]s.
 */
@Composable
fun Timeline(
  postPreviewsLoadable: ListLoadable<PostPreview>,
  onHighlightClick: (URL) -> Unit,
  onFavorite: (id: String) -> Unit,
  onRepost: (id: String) -> Unit,
  onShare: (URL) -> Unit,
  onClick: (id: String) -> Unit,
  onNext: (index: Int) -> Unit,
  modifier: Modifier = Modifier,
  state: LazyListState = rememberLazyListState(),
  contentPadding: PaddingValues = PaddingValues(),
  refresh: Refresh = Refresh.Disabled,
  relativeTimeProvider: RelativeTimeProvider = rememberRelativeTimeProvider(),
  header: (@Composable () -> Unit)? = null
) {
  when (postPreviewsLoadable) {
    is ListLoadable.Empty ->
      EmptyTimelineMessage(header?.let { { it() } }, contentPadding, modifier)
    is ListLoadable.Loading -> Timeline(modifier, contentPadding, header?.let { { it() } })
    is ListLoadable.Populated ->
      Timeline(
        postPreviewsLoadable.content,
        onHighlightClick,
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
    is ListLoadable.Failed -> Unit
  }
}

/**
 * [LazyColumn] for displaying loading [PostPreview]s.
 *
 * @param header [Composable] to be shown above the [PostPreview]s.
 * @param modifier [Modifier] to be applied to the underlying [LazyColumn].
 * @param contentPadding [PaddingValues] to pad the content with.
 */
@Composable
fun Timeline(
  modifier: Modifier = Modifier,
  contentPadding: PaddingValues = PaddingValues(),
  header: @Composable (LazyItemScope.() -> Unit)? = null
) {
  Timeline(onNext = {}, modifier, header, contentPadding = contentPadding) {
    items(128) { PostPreview() }
  }
}

/**
 * [LazyColumn] for displaying paged [PostPreview]s.
 *
 * @param postPreviews [PostPreview]s to be lazily shown.
 * @param onHighlightClick Callback run whenever the [PostPreview]'s [PostPreview.highlight] is
 *   clicked.
 * @param onFavorite Callback run whenever the [PostPreview] associated to the given ID requests the
 *   [Post] to have its "favorited" state toggled.
 * @param onRepost Callback run whenever the [PostPreview] associated to the given ID requests the
 *   [Post] to have its "reblogged" state toggled.
 * @param onShare Callback run whenever a [PostPreview] requests the [Post]'s [URL] is requested to
 *   be shared.
 * @param onClick Callback run whenever the [PostPreview] associated to the given ID is clicked.
 * @param onNext Callback run whenever the bottom is being reached.
 * @param modifier [Modifier] to be applied to the underlying [LazyColumn].
 * @param state [LazyListState] through which scroll will be observed.
 * @param contentPadding [PaddingValues] to pad the content with.
 * @param refresh Configuration for the swipe-to-refresh behavior to be adopted.
 * @param relativeTimeProvider [RelativeTimeProvider] that provides the time that's passed since a
 *   [Post] was published.
 * @param header [Composable] to be shown above the [PostPreview]s.
 */
@Composable
fun Timeline(
  postPreviews: List<PostPreview>,
  onHighlightClick: (URL) -> Unit,
  onFavorite: (id: String) -> Unit,
  onRepost: (id: String) -> Unit,
  onShare: (URL) -> Unit,
  onClick: (id: String) -> Unit,
  onNext: (index: Int) -> Unit,
  modifier: Modifier = Modifier,
  state: LazyListState = rememberLazyListState(),
  contentPadding: PaddingValues = PaddingValues(),
  refresh: Refresh = Refresh.Disabled,
  relativeTimeProvider: RelativeTimeProvider = rememberRelativeTimeProvider(),
  header: (@Composable () -> Unit)? = null
) {
  if (postPreviews.isEmpty()) {
    EmptyTimelineMessage(header?.let { { it() } }, contentPadding, modifier)
  } else {
    Timeline(onNext, modifier, header?.let { { it() } }, state, contentPadding, refresh) {
      itemsIndexed(
        postPreviews,
        key = { _, preview -> preview.id },
        contentType = { _, _ -> TimelineContentType.POST_PREVIEW }
      ) { index, preview ->
        if (index == 0 && header != null || index != 0 && index != postPreviews.lastIndex) {
          Divider(Modifier.testTag(TIMELINE_DIVIDER_TAG))
        }

        PostPreview(
          preview,
          onHighlightClick = { preview.highlight?.url?.run(onHighlightClick) },
          onFavorite = { onFavorite(preview.id) },
          onRepost = { onRepost(preview.id) },
          onShare = { onShare(preview.url) },
          onClick = { onClick(preview.id) },
          relativeTimeProvider = relativeTimeProvider
        )
      }
    }
  }
}

/**
 * [LazyColumn] for displaying paged content.
 *
 * @param onNext Callback run whenever the bottom is being reached.
 * @param modifier [Modifier] to be applied to the underlying [LazyColumn].
 * @param header [Composable] to be shown above the [content].
 * @param state [LazyListState] through which scroll will be observed.
 * @param contentPadding [PaddingValues] to pad the contents ([header] + [content]) with.
 * @param refresh Configuration for the swipe-to-refresh behavior to be adopted.
 * @param content Content to be lazily shown below the [header].
 */
@Composable
@OptIn(ExperimentalMaterialApi::class)
fun Timeline(
  onNext: (index: Int) -> Unit,
  modifier: Modifier = Modifier,
  header: (@Composable LazyItemScope.() -> Unit)? = null,
  state: LazyListState = rememberLazyListState(),
  contentPadding: PaddingValues = PaddingValues(),
  refresh: Refresh = Refresh.Disabled,
  content: LazyListScope.() -> Unit
) {
  val pullRefreshState = rememberPullRefreshState(refresh.isInProgress, refresh.listener::onRefresh)
  var index by rememberSaveable { mutableIntStateOf(0) }
  var hasReachedRenderEffect by remember { mutableStateOf(false) }

  DisposableEffect(Unit) {
    if (!hasReachedRenderEffect) {
      onNext(index++)
    }
    onDispose {}
  }

  Box(Modifier.pullRefresh(pullRefreshState)) {
    LazyColumn(modifier.testTag(TIMELINE_TAG), state, contentPadding) {
      header?.let { item(contentType = TimelineContentType.HEADER, content = it) }
      content()
      renderEffect(
        key = content,
        TimelineContentType.RENDER_EFFECT,
        onPlacement = { hasReachedRenderEffect = true }
      ) {
        /*
         * If the content has filled the entirety of the height of the screen when the timeline was
         * first composed, then the index has already been incremented by the disposable effect
         * above; thus, invoking `onNext` with `++index` in the first branch prevents the same index
         * from being provided twice to the callback.
         *
         * As for the contrary case in the "else" branch, if the content was short enough for the
         * render effect to be visible, then the index hasn't been preemptively incremented, meaning
         * that its current value should be the one to be provided.
         */
        if (hasReachedRenderEffect) {
          onNext(++index)
        } else {
          onNext(index++)
        }
      }
    }

    PullRefreshIndicator(
      refresh.isInProgress,
      pullRefreshState,
      Modifier.offset(y = refresh.indicatorOffset)
        .align(Alignment.TopCenter)
        .testTag(TIMELINE_REFRESH_INDICATOR)
        .semantics { isInProgress = refresh.isInProgress }
    )
  }
}

/**
 * Displays [PostPreview]s in a paginated way.
 *
 * @param postPreviewsLoadable [ListLoadable] of [PostPreview]s to be lazily shown.
 * @param modifier [Modifier] to be applied to the underlying [Timeline].
 * @param header [Composable] to be shown above the [PostPreview]s.
 */
@Composable
internal fun Timeline(
  postPreviewsLoadable: ListLoadable<PostPreview>,
  modifier: Modifier = Modifier,
  header: @Composable (() -> Unit)? = null
) {
  Timeline(
    postPreviewsLoadable,
    onHighlightClick = {},
    onFavorite = {},
    onRepost = {},
    onShare = {},
    onClick = {},
    onNext = {},
    modifier,
    header = header
  )
}

/**
 * [Timeline] that's populated with sample [PostPreview]s.
 *
 * @param postPreviews [PostPreview]s to be lazily shown.
 * @param modifier [Modifier] to be applied to the underlying [Timeline].
 * @param onNext Callback run whenever the bottom is being reached.
 * @param header [Composable] to be shown above the [PostPreview]s.
 * @see PostPreview.samples
 */
@Composable
internal fun Timeline(
  postPreviews: List<PostPreview>,
  modifier: Modifier = Modifier,
  onNext: (index: Int) -> Unit = {},
  header: @Composable() (() -> Unit)? = null
) {
  Timeline(
    postPreviews,
    onHighlightClick = {},
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
 * @param header [Composable] to be shown above the message.
 * @param contentPadding [PaddingValues] to pad the contents with.
 * @param modifier [Modifier] to be applied to the underlying [Column].
 */
@Composable
private fun EmptyTimelineMessage(
  header: (@Composable LazyItemScope.() -> Unit)?,
  contentPadding: PaddingValues,
  modifier: Modifier = Modifier
) {
  val spacing = AutosTheme.spacings.large.dp

  LazyColumn(
    modifier.testTag(EMPTY_TIMELINE_MESSAGE_TAG).fillMaxSize(),
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
            contentDescription = stringResource(R.string.platform_ui_timeline_empty),
            Modifier.size(32.dp),
            tint = AutosTheme.colors.secondary.asColor
          )

          Text(
            stringResource(R.string.platform_ui_timeline_empty_message),
            textAlign = TextAlign.Center,
            style = AutosTheme.typography.headlineMedium
          )
        }
      }
    }
  }
}

/** Preview of a loading [Timeline]. */
@Composable
@MultiThemePreview
private fun LoadingTimelinePreview() {
  AutosTheme { Surface(color = AutosTheme.colors.background.container.asColor) { Timeline() } }
}

/** Preview of an empty [Timeline]. */
@Composable
@MultiThemePreview
private fun EmptyTimelinePreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) {
      Timeline(ListLoadable.Empty())
    }
  }
}

/** Preview of an empty [Timeline] with a header. */
@Composable
@MultiThemePreview
private fun EmptyTimelineWithHeaderPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) {
      Timeline(ListLoadable.Empty()) {
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
  AutosTheme { Surface(color = AutosTheme.colors.background.container.asColor) { Timeline() } }
}

/** Preview of a populated [Timeline] with a header. */
@Composable
@MultiThemePreview
private fun PopulatedTimelineWithHeaderPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) {
      Timeline {
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
