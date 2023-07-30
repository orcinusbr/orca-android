package com.jeanbarrossilva.mastodonte.feature.feed

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import com.jeanbarrossilva.mastodonte.core.sample.profile.toot.samples
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.plus
import com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.Timeline
import com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot.TootPreview
import com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot.loadingTootPreviews
import com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot.toTootPreview
import java.net.URL

@Composable
internal fun Feed(
    viewModel: FeedViewModel,
    boundary: FeedBoundary,
    modifier: Modifier = Modifier
) {
    val tootsLoadable by viewModel.tootsLoadableFlow.collectAsState()

    Feed(
        tootsLoadable,
        onSearch = boundary::navigateToSearch,
        onFavorite = viewModel::favorite,
        onReblog = viewModel::reblog,
        onShare = viewModel::share,
        onTootClick = boundary::navigateToTootDetails,
        onNext = viewModel::loadTootsAt,
        onComposition = boundary::navigateToComposer,
        modifier
    )
}

@Composable
internal fun Feed(
    toots: List<Toot>,
    onSearch: () -> Unit,
    onFavorite: (tootID: String) -> Unit,
    onReblog: (tootID: String) -> Unit,
    onShare: (URL) -> Unit,
    onTootClick: (tootID: String) -> Unit,
    onNext: (index: Int) -> Unit,
    onComposition: () -> Unit,
    modifier: Modifier = Modifier
) {
    Feed(
        onSearch,
        timelineContentPadding = MastodonteTheme.overlays.fab,
        toots = {
            items(toots) {
                TootPreview(
                    it.toTootPreview(),
                    onFavorite = { onFavorite(it.id) },
                    onReblog = { onReblog(it.id) },
                    onShare = { onShare(it.url) },
                    onClick = { onTootClick(it.id) }
                )
            }
        },
        onNext,
        floatingActionButton = {
            FloatingActionButton(onClick = onComposition) {
                Icon(MastodonteTheme.Icons.Create, contentDescription = "Compose")
            }
        },
        modifier
    )
}

@Composable
private fun Feed(
    tootsLoadable: ListLoadable<Toot>,
    onSearch: () -> Unit,
    onFavorite: (tootID: String) -> Unit,
    onReblog: (tootID: String) -> Unit,
    onShare: (URL) -> Unit,
    onTootClick: (tootID: String) -> Unit,
    onNext: (index: Int) -> Unit,
    onComposition: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (tootsLoadable) {
        is ListLoadable.Loading ->
            Feed(onSearch, modifier)
        is ListLoadable.Populated ->
            Feed(
                tootsLoadable.content,
                onSearch,
                onFavorite,
                onReblog,
                onShare,
                onTootClick,
                onNext,
                onComposition,
                modifier
            )
        is ListLoadable.Empty, is ListLoadable.Failed ->
            Unit
    }
}

@Composable
private fun Feed(onSearch: () -> Unit, modifier: Modifier = Modifier) {
    Feed(
        onSearch,
        timelineContentPadding = PaddingValues(0.dp),
        toots = LazyListScope::loadingTootPreviews,
        onNext = { },
        floatingActionButton = { },
        modifier
    )
}

@Composable
private fun Feed(
    onSearch: () -> Unit,
    timelineContentPadding: PaddingValues,
    toots: LazyListScope.() -> Unit,
    onNext: (index: Int) -> Unit,
    floatingActionButton: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier,
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            CenterAlignedTopAppBar(
                title = { Text("Feed") },
                actions = {
                    IconButton(onClick = onSearch) {
                        Icon(MastodonteTheme.Icons.Search, contentDescription = "Search")
                    }
                }
            )
        },
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = FabPosition.Center
    ) {
        Timeline(onNext, contentPadding = it + timelineContentPadding) {
            toots()
        }
    }
}

@Composable
@Preview
private fun LoadingFeedPreview() {
    MastodonteTheme {
        Feed(onSearch = { })
    }
}

@Composable
@Preview
private fun LoadedFeedPreview() {
    MastodonteTheme {
        Feed(
            Toot.samples,
            onSearch = { },
            onFavorite = { },
            onReblog = { },
            onShare = { },
            onTootClick = { },
            onNext = { },
            onComposition = { }
        )
    }
}
