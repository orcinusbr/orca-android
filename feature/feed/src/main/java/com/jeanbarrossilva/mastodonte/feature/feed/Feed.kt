package com.jeanbarrossilva.mastodonte.feature.feed

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
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.loadable.list.toListLoadable
import com.jeanbarrossilva.loadable.list.toSerializableList
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.Timeline
import com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot.TootPreview
import java.net.URL

@Composable
internal fun Feed(
    viewModel: FeedViewModel,
    boundary: FeedBoundary,
    modifier: Modifier = Modifier
) {
    val tootPreviewsLoadable by viewModel.tootPreviewsLoadableFlow.collectAsState()

    Feed(
        tootPreviewsLoadable,
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
private fun Feed(
    tootPreviewsLoadable: ListLoadable<TootPreview>,
    onSearch: () -> Unit,
    onFavorite: (tootID: String) -> Unit,
    onReblog: (tootID: String) -> Unit,
    onShare: (URL) -> Unit,
    onTootClick: (tootID: String) -> Unit,
    onNext: (index: Int) -> Unit,
    onComposition: () -> Unit,
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
        floatingActionButton = {
            if (
                tootPreviewsLoadable is ListLoadable.Empty ||
                tootPreviewsLoadable is ListLoadable.Populated
            ) {
                FloatingActionButton(onClick = onComposition) {
                    Icon(MastodonteTheme.Icons.Create, contentDescription = "Compose")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        Timeline(
            tootPreviewsLoadable,
            onFavorite,
            onReblog,
            onShare,
            onTootClick,
            onNext,
            contentPadding = it
        )
    }
}

@Composable
@Preview
private fun LoadingFeedPreview() {
    MastodonteTheme {
        Feed(ListLoadable.Loading())
    }
}

@Composable
@Preview
private fun LoadedFeedPreview() {
    MastodonteTheme {
        Feed(TootPreview.samples.toSerializableList().toListLoadable())
    }
}

@Composable
private fun Feed(tootPreviewsLoadable: ListLoadable<TootPreview>, modifier: Modifier = Modifier) {
    Feed(
        tootPreviewsLoadable,
        onSearch = { },
        onFavorite = { },
        onReblog = { },
        onShare = { },
        onTootClick = { },
        onNext = { },
        onComposition = { },
        modifier
    )
}
