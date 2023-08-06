package com.jeanbarrossilva.orca.feature.feed

import android.content.res.Configuration
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.loadable.list.toListLoadable
import com.jeanbarrossilva.loadable.list.toSerializableList
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.ui.component.scaffold.bar.TopAppBar
import com.jeanbarrossilva.orca.platform.ui.component.scaffold.bar.TopAppBarDefaults
import com.jeanbarrossilva.orca.platform.ui.component.scaffold.bar.text.AutoSizeText
import com.jeanbarrossilva.orca.platform.ui.component.timeline.Timeline
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TootPreview
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
@OptIn(ExperimentalMaterial3Api::class)
internal fun Feed(
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
    val topAppBarScrollBehavior = TopAppBarDefaults.scrollBehavior

    Scaffold(
        modifier,
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { AutoSizeText("Feed") },
                actions = {
                    IconButton(onClick = onSearch) {
                        Icon(OrcaTheme.Icons.Search, contentDescription = "Search")
                    }
                },
                scrollBehavior = topAppBarScrollBehavior
            )
        },
        floatingActionButton = {
            if (tootPreviewsLoadable.isLoaded) {
                FloatingActionButton(onClick = onComposition) {
                    Icon(OrcaTheme.Icons.Create, contentDescription = "Compose")
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
            Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
            contentPadding = it
        )
    }
}

@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun LoadingFeedPreview() {
    OrcaTheme {
        Feed(ListLoadable.Loading())
    }
}

@Composable
@Preview
private fun EmptyFeedPreview() {
    OrcaTheme {
        Feed(ListLoadable.Empty())
    }
}

@Composable
@Preview
private fun PopulatedFeedPreview() {
    OrcaTheme {
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
