package com.jeanbarrossilva.mastodonte.feature.tootdetails

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.mastodonte.feature.tootdetails.ui.header.Header
import com.jeanbarrossilva.mastodonte.feature.tootdetails.viewmodel.TootDetailsViewModel
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.backwardsNavigationArrow
import com.jeanbarrossilva.mastodonte.platform.ui.timeline.Timeline
import com.jeanbarrossilva.mastodonte.platform.ui.timeline.toot.TootPreview
import com.jeanbarrossilva.mastodonte.platform.ui.timeline.toot.loadingTootPreviews
import java.net.URL

@Composable
fun TootDetails(
    viewModel: TootDetailsViewModel,
    navigator: TootDetailsNavigator,
    modifier: Modifier = Modifier
) {
    val tootLoadable by viewModel.tootLoadableFlow.collectAsState()
    val commentsLoadable by viewModel.commentsLoadableFlow.collectAsState()

    TootDetails(
        tootLoadable,
        commentsLoadable,
        onFavorite = viewModel::favorite,
        onReblog = viewModel::reblog,
        onShare = viewModel::share,
        onNavigateToDetails = navigator::navigateToTootDetails,
        onNext = viewModel::loadCommentsAt,
        onBackwardsNavigation = navigator::pop,
        modifier
    )
}

@Composable
private fun TootDetails(
    tootLoadable: Loadable<Toot>,
    commentsLoadable: ListLoadable<Toot>,
    onFavorite: (id: String) -> Unit,
    onReblog: (id: String) -> Unit,
    onShare: (URL) -> Unit,
    onNavigateToDetails: (id: String) -> Unit,
    onNext: (index: Int) -> Unit,
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
                onFavorite,
                onReblog,
                onShare,
                onNavigateToDetails,
                onNext,
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
        onNext = { },
        onBackwardsNavigation,
        modifier
    )
}

@Composable
private fun TootDetails(
    toot: Toot,
    commentsLoadable: ListLoadable<Toot>,
    onFavorite: (id: String) -> Unit,
    onReblog: (id: String) -> Unit,
    onShare: (URL) -> Unit,
    onNavigateToDetails: (id: String) -> Unit,
    onNext: (index: Int) -> Unit,
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
                            onFavorite = { onFavorite(it.id) },
                            onReblog = { onReblog(it.id) },
                            onShare = { onShare(it.url) },
                            onClick = { onNavigateToDetails(it.id) }
                        )
                    }
                is ListLoadable.Empty, is ListLoadable.Failed ->
                    Unit
            }
        },
        onNext,
        onBackwardsNavigation,
        modifier
    )
}

@Composable
private fun TootDetails(
    header: @Composable () -> Unit,
    comments: LazyListScope.() -> Unit,
    onNext: (index: Int) -> Unit,
    onBackwardsNavigation: () -> Unit,
    modifier: Modifier = Modifier
) {
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
        Timeline(onNext, contentPadding = it) {
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
            onFavorite = { },
            onReblog = { },
            onShare = { },
            onNavigateToDetails = { },
            onNext = { },
            onBackwardsNavigation = { }
        )
    }
}
