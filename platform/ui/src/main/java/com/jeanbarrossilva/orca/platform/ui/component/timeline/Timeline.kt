package com.jeanbarrossilva.orca.platform.ui.component.timeline

import androidx.annotation.RestrictTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.top.text.AutoSizeText
import com.jeanbarrossilva.orca.platform.ui.R
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.SampleTootPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TootPreview
import java.net.URL

/** Tag that identifies an [EmptyTimelineMessage] for testing purposes. */
internal const val EMPTY_TIMELINE_MESSAGE_TAG = "empty-timeline-tag"

/**
 * Tag that identifies dividers between [SampleTootPreview]s in a [Timeline] for testing purposes.
 */
internal const val TIMELINE_DIVIDER_TAG = "timeline-divider"

/** Tag that identifies a [Timeline] for testing purposes. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX) const val TIMELINE_TAG = "timeline"

/** [Timeline] content types for Compose to reuse the [Composable]s while lazily displaying them. */
private enum class TimelineContentType {
  /** Content type for the header. */
  HEADER,

  /** Content type for [SampleTootPreview]s. */
  TOOT_PREVIEW
}

/**
 * Displays [SampleTootPreview]s in a paginated way.
 *
 * @param tootPreviewsLoadable [ListLoadable] of [SampleTootPreview]s to be lazily shown.
 * @param onHighlightClick Callback run whenever the [SampleTootPreview]'s
 *   [SampleTootPreview.highlight] is clicked.
 * @param onFavorite Callback run whenever the [SampleTootPreview] associated to the given ID
 *   requests the [Toot] to have its "favorited" state toggled.
 * @param onReblog Callback run whenever the [SampleTootPreview] associated to the given ID requests
 *   the [Toot] to have its "reblogged" state toggled.
 * @param onShare Callback run whenever a [SampleTootPreview] requests the [Toot]'s [URL] is
 *   requested to be shared.
 * @param onClick Callback run whenever the [SampleTootPreview] associated to the given ID is
 *   clicked.
 * @param onNext Callback run whenever the user reaches the bottom.
 * @param modifier [Modifier] to be applied to the underlying [LazyColumn].
 * @param state [LazyListState] through which scroll will be observed.
 * @param contentPadding [PaddingValues] to pad the content with.
 * @param header [Composable] to be shown above the [SampleTootPreview]s.
 */
@Composable
fun Timeline(
  tootPreviewsLoadable: ListLoadable<TootPreview>,
  onHighlightClick: (URL) -> Unit,
  onFavorite: (id: String) -> Unit,
  onReblog: (id: String) -> Unit,
  onShare: (URL) -> Unit,
  onClick: (id: String) -> Unit,
  onNext: (index: Int) -> Unit,
  modifier: Modifier = Modifier,
  state: LazyListState = rememberLazyListState(),
  contentPadding: PaddingValues = PaddingValues(),
  header: (@Composable LazyItemScope.() -> Unit)? = null
) {
  when (tootPreviewsLoadable) {
    is ListLoadable.Empty -> EmptyTimelineMessage(header, contentPadding, modifier)
    is ListLoadable.Loading -> Timeline(modifier, contentPadding, header)
    is ListLoadable.Populated ->
      Timeline(
        tootPreviewsLoadable.content,
        onHighlightClick,
        onFavorite,
        onReblog,
        onShare,
        onClick,
        onNext,
        modifier,
        state,
        contentPadding,
        header
      )
    is ListLoadable.Failed -> Unit
  }
}

/**
 * [LazyColumn] for displaying loading [SampleTootPreview]s.
 *
 * @param header [Composable] to be shown above the [SampleTootPreview]s.
 * @param modifier [Modifier] to be applied to the underlying [LazyColumn].
 * @param contentPadding [PaddingValues] to pad the content with.
 */
@Composable
fun Timeline(
  modifier: Modifier = Modifier,
  contentPadding: PaddingValues = PaddingValues(),
  header: @Composable (LazyItemScope.() -> Unit)? = null
) {
  Timeline(onNext = {}, header, modifier, contentPadding = contentPadding) {
    items(128) { TootPreview() }
  }
}

/**
 * [LazyColumn] for displaying paged [SampleTootPreview]s.
 *
 * @param tootPreviews [SampleTootPreview]s to be lazily shown.
 * @param onHighlightClick Callback run whenever the [SampleTootPreview]'s
 *   [SampleTootPreview.highlight] is clicked.
 * @param onFavorite Callback run whenever the [SampleTootPreview] associated to the given ID
 *   requests the [Toot] to have its "favorited" state toggled.
 * @param onReblog Callback run whenever the [SampleTootPreview] associated to the given ID requests
 *   the [Toot] to have its "reblogged" state toggled.
 * @param onShare Callback run whenever a [SampleTootPreview] requests the [Toot]'s [URL] is
 *   requested to be shared.
 * @param onClick Callback run whenever the [SampleTootPreview] associated to the given ID is
 *   clicked.
 * @param onNext Callback run whenever the user reaches the bottom.
 * @param modifier [Modifier] to be applied to the underlying [LazyColumn].
 * @param state [LazyListState] through which scroll will be observed.
 * @param contentPadding [PaddingValues] to pad the content with.
 * @param header [Composable] to be shown above the [SampleTootPreview]s.
 */
@Composable
fun Timeline(
  tootPreviews: List<TootPreview>,
  onHighlightClick: (URL) -> Unit,
  onFavorite: (id: String) -> Unit,
  onReblog: (id: String) -> Unit,
  onShare: (URL) -> Unit,
  onClick: (id: String) -> Unit,
  onNext: (index: Int) -> Unit,
  modifier: Modifier = Modifier,
  state: LazyListState = rememberLazyListState(),
  contentPadding: PaddingValues = PaddingValues(),
  header: (@Composable LazyItemScope.() -> Unit)? = null
) {
  if (tootPreviews.isEmpty()) {
    EmptyTimelineMessage(header, contentPadding, modifier)
  } else {
    Timeline(onNext, header, modifier, state, contentPadding) {
      itemsIndexed(
        tootPreviews,
        key = { _, preview -> preview.id },
        contentType = { _, _ -> TimelineContentType.TOOT_PREVIEW }
      ) { index, preview ->
        if (index == 0 && header != null || index != 0 && index != tootPreviews.lastIndex) {
          Divider(Modifier.testTag(TIMELINE_DIVIDER_TAG))
        }

        TootPreview(
          preview,
          onHighlightClick = { preview.highlight?.url?.run(onHighlightClick) },
          onFavorite = { onFavorite(preview.id) },
          onReblog = { onReblog(preview.id) },
          onShare = { onShare(preview.url) },
          onClick = { onClick(preview.id) }
        )
      }
    }
  }
}

/**
 * Displays [SampleTootPreview]s in a paginated way.
 *
 * @param tootPreviewsLoadable [ListLoadable] of [SampleTootPreview]s to be lazily shown.
 * @param modifier [Modifier] to be applied to the underlying [Timeline].
 */
@Composable
internal fun Timeline(
  tootPreviewsLoadable: ListLoadable<TootPreview>,
  modifier: Modifier = Modifier
) {
  Timeline(
    tootPreviewsLoadable,
    onHighlightClick = {},
    onFavorite = {},
    onReblog = {},
    onShare = {},
    onClick = {},
    onNext = {},
    modifier
  )
}

/**
 * [Timeline] that's populated with sample [SampleTootPreview]s.
 *
 * @param modifier [Modifier] to be applied to the underlying [Timeline].
 * @param tootPreviews [SampleTootPreview]s to be lazily shown.
 * @param header [Composable] to be shown above the [SampleTootPreview]s.
 * @see SampleTootPreview.samples
 */
@Composable
internal fun PopulatedTimeline(
  modifier: Modifier = Modifier,
  tootPreviews: List<TootPreview> = TootPreview.samples,
  header: @Composable (LazyItemScope.() -> Unit)? = null
) {
  Timeline(
    tootPreviews,
    onHighlightClick = {},
    onFavorite = {},
    onReblog = {},
    onShare = {},
    onClick = {},
    onNext = {},
    modifier,
    header = header
  )
}

/**
 * [LazyColumn] for displaying paged content.
 *
 * @param onNext Callback run whenever the user reaches the bottom.
 * @param header [Composable] to be shown above the [content].
 * @param modifier [Modifier] to be applied to the underlying [LazyColumn].
 * @param state [LazyListState] through which scroll will be observed.
 * @param contentPadding [PaddingValues] to pad the contents ([header] + [content]) with.
 * @param content Content to be lazily shown below the [header].
 */
@Composable
private fun Timeline(
  onNext: (index: Int) -> Unit,
  header: (@Composable LazyItemScope.() -> Unit)?,
  modifier: Modifier = Modifier,
  state: LazyListState = rememberLazyListState(),
  contentPadding: PaddingValues = PaddingValues(),
  content: LazyListScope.() -> Unit
) {
  val shouldLoad = remember(state) { !state.canScrollForward }
  var index by remember { mutableIntStateOf(0) }

  DisposableEffect(shouldLoad) {
    onNext(index++)
    onDispose {}
  }

  LazyColumn(modifier.testTag(TIMELINE_TAG), state, contentPadding) {
    header?.let { item(contentType = { TimelineContentType.HEADER }, content = it) }

    content()
  }
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
  val spacing = OrcaTheme.spacings.large

  LazyColumn(
    modifier.testTag(EMPTY_TIMELINE_MESSAGE_TAG).fillMaxSize(),
    contentPadding = contentPadding,
    verticalArrangement = Arrangement.spacedBy(spacing),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    header?.let { item(content = it) }

    item {
      Icon(
        OrcaTheme.iconography.empty,
        contentDescription = stringResource(R.string.platform_ui_timeline_empty),
        Modifier.padding(start = spacing, top = spacing, end = spacing).size(32.dp),
        tint = OrcaTheme.colors.secondary
      )
    }

    item {
      Text(
        stringResource(R.string.platform_ui_timeline_empty_message),
        Modifier.padding(start = spacing, end = spacing, bottom = spacing),
        textAlign = TextAlign.Center,
        style = OrcaTheme.typography.headlineMedium
      )
    }
  }
}

/** Preview of a loading [Timeline]. */
@Composable
@MultiThemePreview
private fun LoadingTimelinePreview() {
  OrcaTheme { Surface(color = OrcaTheme.colors.background.container) { Timeline() } }
}

/** Preview of an empty [Timeline]. */
@Composable
@MultiThemePreview
private fun EmptyTimelinePreview() {
  OrcaTheme {
    Surface(color = OrcaTheme.colors.background.container) {
      Timeline(
        ListLoadable.Empty(),
        onHighlightClick = {},
        onFavorite = {},
        onReblog = {},
        onShare = {},
        onClick = {},
        onNext = {}
      )
    }
  }
}

/** Preview of a populated [Timeline]. */
@Composable
@MultiThemePreview
private fun PopulatedTimelinePreview() {
  OrcaTheme { Surface(color = OrcaTheme.colors.background.container) { PopulatedTimeline() } }
}

/** Preview of a populated [Timeline] with a header. */
@Composable
@MultiThemePreview
private fun PopulatedTimelineWithHeaderPreview() {
  OrcaTheme {
    Surface(color = OrcaTheme.colors.background.container) {
      PopulatedTimeline {
        AutoSizeText(
          "Header",
          Modifier.padding(
            start = OrcaTheme.spacings.large,
            top = OrcaTheme.spacings.large,
            end = OrcaTheme.spacings.large,
            bottom = OrcaTheme.spacings.medium
          ),
          style = OrcaTheme.typography.displayLarge
        )
      }
    }
  }
}
