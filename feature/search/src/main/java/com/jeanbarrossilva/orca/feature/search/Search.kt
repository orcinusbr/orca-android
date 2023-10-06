package com.jeanbarrossilva.orca.feature.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.sample.feed.profile.search.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.search.samples
import com.jeanbarrossilva.orca.feature.search.ui.SearchResultCard
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.kit.action.button.HoverableIconButton
import com.jeanbarrossilva.orca.platform.theme.kit.input.TextField
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.Scaffold
import com.jeanbarrossilva.orca.platform.ui.core.requestFocusWithDelay

internal object SearchDefaults {
    val VerticalArrangement = Arrangement.Top
    val HorizontalAlignment = Alignment.Start
}

@Composable
internal fun Search(
    viewModel: SearchViewModel,
    boundary: SearchBoundary,
    modifier: Modifier = Modifier
) {
    val query by viewModel.queryFlow.collectAsState()
    val resultsLoadable by viewModel.resultsFlow.collectAsState()

    when (
        @Suppress("NAME_SHADOWING")
        val resultsLoadable = resultsLoadable
    ) {
        is Loadable.Loading ->
            Search(
                query,
                onQueryChange = viewModel::setQuery,
                onBackwardsNavigation = boundary::pop
            )
        is Loadable.Loaded ->
            Search(
                query,
                onQueryChange = viewModel::setQuery,
                resultsLoadable.content,
                onNavigateToProfileDetails = boundary::navigateToProfileDetails,
                onBackwardsNavigation = boundary::pop,
                modifier
            )
        is Loadable.Failed ->
            Unit
    }
}

@Composable
private fun Search(
    query: String,
    onQueryChange: (query: String) -> Unit,
    onBackwardsNavigation: () -> Unit,
    modifier: Modifier = Modifier
) {
    Search(query, onQueryChange, onBackwardsNavigation, modifier) {
        items(128) {
            SearchResultCard()
        }
    }
}

@Composable
private fun Search(
    query: String,
    onQueryChange: (query: String) -> Unit,
    results: List<ProfileSearchResult>,
    onNavigateToProfileDetails: (id: String) -> Unit,
    onBackwardsNavigation: () -> Unit,
    modifier: Modifier = Modifier
) {
    @OptIn(ExperimentalComposeUiApi::class)
    val softwareKeyboardController = LocalSoftwareKeyboardController.current

    val areResultsEmpty = remember(query, results) { query.isNotBlank() && results.isEmpty() }

    Search(
        query,
        onQueryChange,
        onBackwardsNavigation,
        modifier,
        verticalArrangement = if (areResultsEmpty) {
            Arrangement.spacedBy(OrcaTheme.spacings.medium, Alignment.CenterVertically)
        } else {
            SearchDefaults.VerticalArrangement
        },
        horizontalAlignment = if (areResultsEmpty) {
            Alignment.CenterHorizontally
        } else {
            SearchDefaults.HorizontalAlignment
        }
    ) {
        if (areResultsEmpty) {
            item {
                EmptyResultsMessage()
            }
        } else {
            items(results) {
                SearchResultCard(
                    it,
                    onClick = {
                        onNavigateToProfileDetails(it.id)

                        @OptIn(ExperimentalComposeUiApi::class)
                        softwareKeyboardController?.hide()
                    }
                )
            }
        }
    }
}

@Composable
private fun Search(
    query: String,
    onQueryChange: (query: String) -> Unit,
    onBackwardsNavigation: () -> Unit,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = SearchDefaults.VerticalArrangement,
    horizontalAlignment: Alignment.Horizontal = SearchDefaults.HorizontalAlignment,
    content: LazyListScope.() -> Unit
) {
    val focusRequester = remember(::FocusRequester)
    val spacing = OrcaTheme.spacings.medium

    LaunchedEffect(Unit) {
        focusRequester.requestFocusWithDelay()
    }

    Scaffold(
        modifier,
        topAppBar = {
            Row(
                Modifier
                    .background(OrcaTheme.colors.background.container)
                    .padding(top = spacing, end = spacing, bottom = spacing)
                    .statusBarsPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.fillMaxWidth(.2f), Alignment.Center) {
                    HoverableIconButton(onClick = onBackwardsNavigation) {
                        Icon(
                            OrcaTheme.iconography.back,
                            contentDescription = stringResource(
                                com.jeanbarrossilva.orca.platform.theme.R.string.platform_ui_top_app_bar_back_navigation
                            ),
                            tint = OrcaTheme.colors.background.content
                        )
                    }
                }

                TextField(
                    query,
                    onQueryChange,
                    Modifier
                        .focusRequester(focusRequester)
                        .fillMaxWidth()
                ) {
                    Text(stringResource(R.string.search_placeholder))
                }
            }
        }
    ) {
        LazyColumn(
            @OptIn(ExperimentalLayoutApi::class)
            Modifier
                // TODO: Add bottom bar overlay to Overlays.
                .consumeWindowInsets(PaddingValues(104.dp))
                .imePadding()
                .fillMaxSize(),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            contentPadding = it,
            content = content
        )
    }
}

@Composable
private fun EmptyResultsMessage(modifier: Modifier = Modifier) {
    Column(
        modifier,
        Arrangement.spacedBy(OrcaTheme.spacings.medium),
        Alignment.CenterHorizontally
    ) {
        CompositionLocalProvider(
            LocalContentColor provides OrcaTheme.typography.headlineMedium.color
        ) {
            Icon(
                OrcaTheme.iconography.search,
                contentDescription = stringResource(R.string.search),
                Modifier.size(64.dp)
            )

            Text(
                stringResource(R.string.search_no_results_found),
                style = OrcaTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
@MultiThemePreview
private fun LoadingSearchPreview() {
    OrcaTheme {
        Search(query = "", onQueryChange = { }, onBackwardsNavigation = { })
    }
}

@Composable
@MultiThemePreview
private fun EmptyResultsPreview() {
    OrcaTheme {
        Search(
            query = "${ProfileSearchResult.sample.account}",
            onQueryChange = { },
            results = emptyList(),
            onNavigateToProfileDetails = { },
            onBackwardsNavigation = { }
        )
    }
}

@Composable
@MultiThemePreview
private fun LoadedSearchPreview() {
    OrcaTheme {
        Search(
            query = "",
            onQueryChange = { },
            ProfileSearchResult.samples,
            onNavigateToProfileDetails = { },
            onBackwardsNavigation = { }
        )
    }
}
