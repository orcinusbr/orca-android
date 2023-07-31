package com.jeanbarrossilva.mastodonte.platform.ui.component.timeline

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.mastodonte.core.feed.profile.toot.Toot
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot.TootPreview
import java.net.URL

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
 * [LazyColumn] for displaying paged [TootPreview]s.
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
        is ListLoadable.Empty, is ListLoadable.Failed ->
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

    LazyColumn(modifier, state, contentPadding) {
        header?.let {
            item(contentType = { TimelineContentType.HEADER }, content = it)
        }

        content()
    }
}

/** Preview of a loading [Timeline]. **/
@Composable
@Preview
private fun LoadingTimelinePreview() {
    MastodonteTheme {
        Surface(color = MastodonteTheme.colorScheme.background) {
            Timeline()
        }
    }
}

/** Preview of a loaded [Timeline]. **/
@Composable
@Preview
private fun LoadedTimelinePreview() {
    MastodonteTheme {
        Surface(color = MastodonteTheme.colorScheme.background) {
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
