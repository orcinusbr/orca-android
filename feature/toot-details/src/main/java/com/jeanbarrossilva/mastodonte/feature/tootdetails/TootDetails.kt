package com.jeanbarrossilva.mastodonte.feature.tootdetails

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.mastodonte.feature.tootdetails.ui.header.Header
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.backwardsNavigationArrow
import com.jeanbarrossilva.mastodonte.platform.ui.timeline.toot.TootPreview
import com.jeanbarrossilva.mastodonte.platform.ui.timeline.toot.loadingTootPreviews
import java.net.URL

@Composable
private fun TootDetails(
    tootLoadable: Loadable<Toot>,
    commentsLoadable: ListLoadable<Toot>,
    onFavoriteToot: (id: String) -> Unit,
    onReblogToot: (id: String) -> Unit,
    onShare: (URL) -> Unit,
    onNavigateToTootDetails: (id: String) -> Unit,
    onLoad: () -> Unit,
    onBackwardsNavigation: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (tootLoadable) {
        is Loadable.Loading ->
            TootDetails(onBackwardsNavigation, modifier)
        is Loadable.Loaded ->
            TootDetails(
                tootLoadable.content,
                commentsLoadable,
                onFavoriteToot,
                onReblogToot,
                onShare,
                onNavigateToTootDetails,
                onLoad,
                onBackwardsNavigation,
                modifier
            )
        is Loadable.Failed ->
            Unit
    }
}

@Composable
private fun TootDetails(onBackwardsNavigation: () -> Unit, modifier: Modifier = Modifier) {
    TootDetails(
        header = { Header() },
        comments = { loadingTootPreviews() },
        onLoad = { },
        onBackwardsNavigation,
        modifier
    )
}

@Composable
private fun TootDetails(
    toot: Toot,
    commentsLoadable: ListLoadable<Toot>,
    onFavoriteToot: (id: String) -> Unit,
    onReblogToot: (id: String) -> Unit,
    onShare: (URL) -> Unit,
    onNavigateToTootDetails: (id: String) -> Unit,
    onLoad: () -> Unit,
    onBackwardsNavigation: () -> Unit,
    modifier: Modifier = Modifier
) {
    TootDetails(
        header = { Header(toot, onShare = { onShare(toot.url) }) },
        comments = {
            when (
                @Suppress("NAME_SHADOWING")
                val commentsLoadable = commentsLoadable
            ) {
                is ListLoadable.Loading ->
                    loadingTootPreviews()
                is ListLoadable.Populated ->
                    items(commentsLoadable.content) {
                        TootPreview(
                            it.toTootPreview(),
                            onFavorite = { onFavoriteToot(it.id) },
                            onReblog = { onReblogToot(it.id) },
                            onShare = { onShare(it.url) },
                            onClick = { onNavigateToTootDetails(it.id) }
                        )
                    }
                is ListLoadable.Empty, is ListLoadable.Failed ->
                    Unit
            }
        },
        onLoad,
        onBackwardsNavigation,
        modifier
    )
}

@Composable
private fun TootDetails(
    header: @Composable () -> Unit,
    comments: LazyListScope.() -> Unit,
    onLoad: () -> Unit,
    onBackwardsNavigation: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()
    val shouldLoad = remember(lazyListState) { !lazyListState.canScrollForward }

    DisposableEffect(shouldLoad) {
        onLoad()
        onDispose { }
    }

    Scaffold(
        modifier,
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            CenterAlignedTopAppBar(
                title = { Text("Toot") },
                navigationIcon = {
                    IconButton(onClick = onBackwardsNavigation) {
                        Icon(
                            MastodonteTheme.Icons.backwardsNavigationArrow,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(state = lazyListState, contentPadding = it) {
            item { header() }
            comments()
        }
    }
}

@Composable
@Preview
private fun LoadingTootDetailsPreview() {
    MastodonteTheme {
        TootDetails(onBackwardsNavigation = { })
    }
}

@Composable
@Preview
private fun LoadedTootDetailsPreview() {
    MastodonteTheme {
        TootDetails(
            Toot.sample,
            commentsLoadable = ListLoadable.Loading(),
            onFavoriteToot = { },
            onReblogToot = { },
            onShare = { },
            onNavigateToTootDetails = { },
            onLoad = { },
            onBackwardsNavigation = { }
        )
    }
}
