package com.jeanbarrossilva.orca.platform.ui.component.timeline

import android.content.res.Configuration
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.ui.R
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TootPreview
import java.net.URL

/** Tag that identifies an [EmptyTimelineMessage] for testing purposes. **/
internal const val EMPTY_TIMELINE_MESSAGE_TAG = "empty-timeline-tag"

/** Tag that identifies a [Timeline] for testing purposes. **/
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
const val TIMELINE_TAG = "timeline"

/**
 * [Timeline] content types for Compose to reuse the [Composable]s while lazily displaying them.
 **/
private enum class TimelineContentType {
    /** Content type for the header. **/
    HEADER,

    /** Content type for [TootPreview]s. **/
    TOOT_PREVIEW
}

/**
 * Displays [TootPreview]s in a paginated way.
 *
 * @param tootPreviewsLoadable [ListLoadable] of [TootPreview]s to be lazily shown.
 * @param onFavorite Callback run whenever the [TootPreview] associated to the given ID requests the
 * [Toot] to have its "favorited" state toggled.
 * @param onReblog Callback run whenever the [TootPreview] associated to the given ID requests the
 * [Toot] to have its "reblogged" state toggled.
 * @param onShare Callback run whenever a [TootPreview] requests the [Toot]'s [URL] is requested to
 * be shared.
 * @param onClick Callback run whenever the [TootPreview] associated to the given ID is clicked.
 * @param onNext Callback run whenever the user reaches the bottom.
 * @param modifier [Modifier] to be applied to the underlying [LazyColumn].
 * @param state [LazyListState] through which scroll will be observed.
 * @param contentPadding [PaddingValues] to pad the content with.
 * @param header [Composable] to be shown above the [TootPreview]s.
 **/
@Composable
fun Timeline(
    tootPreviewsLoadable: ListLoadable<TootPreview>,
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
        is ListLoadable.Empty ->
            EmptyTimelineMessage(header, modifier)
        is ListLoadable.Loading ->
            Timeline(modifier, contentPadding, header)
        is ListLoadable.Populated ->
            Timeline(
                tootPreviewsLoadable.content,
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
        is ListLoadable.Failed ->
            Unit
    }
}

/**
 * [LazyColumn] for displaying loading [TootPreview]s.
 *
 * @param header [Composable] to be shown above the [TootPreview]s.
 * @param modifier [Modifier] to be applied to the underlying [LazyColumn].
 * @param contentPadding [PaddingValues] to pad the content with.
 **/
@Composable
fun Timeline(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    header: @Composable (LazyItemScope.() -> Unit)? = null
) {
    Timeline(onNext = { }, header, modifier, contentPadding = contentPadding) {
        items(128) {
            TootPreview()
        }
    }
}

/**
 * [LazyColumn] for displaying paged [TootPreview]s.
 *
 * @param tootPreviews [TootPreview]s to be lazily shown.
 * @param onFavorite Callback run whenever the [TootPreview] associated to the given ID requests the
 * [Toot] to have its "favorited" state toggled.
 * @param onReblog Callback run whenever the [TootPreview] associated to the given ID requests the
 * [Toot] to have its "reblogged" state toggled.
 * @param onShare Callback run whenever a [TootPreview] requests the [Toot]'s [URL] is requested to
 * be shared.
 * @param onClick Callback run whenever the [TootPreview] associated to the given ID is clicked.
 * @param onNext Callback run whenever the user reaches the bottom.
 * @param modifier [Modifier] to be applied to the underlying [LazyColumn].
 * @param state [LazyListState] through which scroll will be observed.
 * @param contentPadding [PaddingValues] to pad the content with.
 * @param header [Composable] to be shown above the [TootPreview]s.
 **/
@Composable
fun Timeline(
    tootPreviews: List<TootPreview>,
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
        EmptyTimelineMessage(header, modifier)
    } else {
        Timeline(onNext, header, modifier, state, contentPadding) {
            items(
                tootPreviews,
                key = TootPreview::id,
                contentType = { TimelineContentType.TOOT_PREVIEW }
            ) {
                TootPreview(
                    it,
                    onFavorite = { onFavorite(it.id) },
                    onReblog = { onReblog(it.id) },
                    onShare = { onShare(it.url) },
                    onClick = { onClick(it.id) }
                )
            }
        }
    }
}

/**
 * Displays [TootPreview]s in a paginated way.
 *
 * @param tootPreviewsLoadable [ListLoadable] of [TootPreview]s to be lazily shown.
 * @param modifier [Modifier] to be applied to the underlying [Timeline].
 */
@Composable
internal fun Timeline(
    tootPreviewsLoadable: ListLoadable<TootPreview>,
    modifier: Modifier = Modifier
) {
    Timeline(
        tootPreviewsLoadable,
        onFavorite = { },
        onReblog = { },
        onShare = { },
        onClick = { },
        onNext = { },
        modifier
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
 **/
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
        onDispose { }
    }

    LazyColumn(modifier.testTag(TIMELINE_TAG), state, contentPadding) {
        header?.let {
            item(contentType = { TimelineContentType.HEADER }, content = it)
        }

        content()
    }
}

/**
 * Message stating that the [Timeline] is empty.
 *
 * @param header [Composable] to be shown above the message.
 * @param modifier [Modifier] to be applied to the underlying [Column].
 **/
@Composable
private fun EmptyTimelineMessage(
    header: (@Composable LazyItemScope.() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val spacing = OrcaTheme.spacings.extraLarge
    val spec = remember { LottieCompositionSpec.RawRes(R.raw.illustration_timeline_empty) }
    val lottieComposition by rememberLottieComposition(spec)

    LazyColumn(
        modifier
            .testTag(EMPTY_TIMELINE_MESSAGE_TAG)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(spacing, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        header?.let {
            item(content = it)
        }

        item {
            LottieAnimation(
                lottieComposition,
                Modifier
                    .padding(start = spacing, top = spacing, end = spacing)
                    .size(256.dp),
                clipToCompositionBounds = true
            )
        }

        item {
            Text(
                "Seems a bit empty in here...",
                Modifier.padding(start = spacing, end = spacing, bottom = spacing),
                textAlign = TextAlign.Center,
                style = OrcaTheme.typography.headlineMedium
            )
        }
    }
}

/** Preview of a loading [Timeline]. **/
@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun LoadingTimelinePreview() {
    OrcaTheme {
        Surface(color = OrcaTheme.colorScheme.background) {
            Timeline()
        }
    }
}

/** Preview of an empty [Timeline]. **/
@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun EmptyTimelinePreview() {
    OrcaTheme {
        Surface(color = OrcaTheme.colorScheme.background) {
            Timeline(
                ListLoadable.Empty(),
                onFavorite = { },
                onReblog = { },
                onShare = { },
                onClick = { },
                onNext = { }
            )
        }
    }
}

/** Preview of a populated [Timeline]. **/
@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun PopulatedTimelinePreview() {
    OrcaTheme {
        Surface(color = OrcaTheme.colorScheme.background) {
            Timeline(
                TootPreview.samples,
                onFavorite = { },
                onReblog = { },
                onShare = { },
                onClick = { },
                onNext = { }
            )
        }
    }
}
