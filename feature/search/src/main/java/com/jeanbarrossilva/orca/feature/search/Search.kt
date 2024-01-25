/*
 * Copyright © 2023-2024 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.feature.search.ui.SearchResultCard
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.kit.input.text.FormTextField
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.Scaffold
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.BackAction
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.core.sample
import com.jeanbarrossilva.orca.platform.focus.rememberImmediateFocusRequester

internal object SearchDefaults {
  val VerticalArrangement = Arrangement.Top
  val HorizontalAlignment = Alignment.Start
}

@Composable
internal fun Search(
  viewModel: SearchViewModel,
  boundary: SearchBoundary,
  onBackwardsNavigation: () -> Unit,
  modifier: Modifier = Modifier
) {
  val query by viewModel.queryFlow.collectAsState()
  val resultsLoadable by viewModel.resultsFlow.collectAsState()

  when (@Suppress("NAME_SHADOWING") val resultsLoadable = resultsLoadable) {
    is Loadable.Loading -> Search(query, onQueryChange = viewModel::setQuery, onBackwardsNavigation)
    is Loadable.Loaded ->
      Search(
        query,
        onQueryChange = viewModel::setQuery,
        resultsLoadable.content,
        onNavigateToProfileDetails = boundary::navigateToProfileDetails,
        onBackwardsNavigation,
        modifier
      )
    is Loadable.Failed -> Unit
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
    items(128) { SearchResultCard() }
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
  val softwareKeyboardController = LocalSoftwareKeyboardController.current

  val areResultsEmpty = remember(query, results) { query.isNotBlank() && results.isEmpty() }

  Search(
    query,
    onQueryChange,
    onBackwardsNavigation,
    modifier,
    verticalArrangement =
      if (areResultsEmpty) {
        Arrangement.spacedBy(AutosTheme.spacings.medium.dp, Alignment.CenterVertically)
      } else {
        SearchDefaults.VerticalArrangement
      },
    horizontalAlignment =
      if (areResultsEmpty) {
        Alignment.CenterHorizontally
      } else {
        SearchDefaults.HorizontalAlignment
      }
  ) {
    if (areResultsEmpty) {
      item { EmptyResultsMessage() }
    } else {
      items(results) {
        SearchResultCard(
          it,
          onClick = {
            onNavigateToProfileDetails(it.id)
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
  val focusRequester = rememberImmediateFocusRequester()
  val spacing = AutosTheme.spacings.medium.dp

  Scaffold(
    modifier,
    topAppBar = {
      Row(
        Modifier.background(AutosTheme.colors.background.container.asColor)
          .padding(top = spacing, end = spacing, bottom = spacing)
          .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(Modifier.fillMaxWidth(.2f), Alignment.Center) {
          BackAction(onClick = onBackwardsNavigation)
        }

        FormTextField(
          query,
          onQueryChange,
          Modifier.focusRequester(focusRequester).fillMaxWidth()
        ) {
          Text(stringResource(R.string.feature_search_placeholder))
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
    Arrangement.spacedBy(AutosTheme.spacings.medium.dp),
    Alignment.CenterHorizontally
  ) {
    CompositionLocalProvider(
      LocalContentColor provides AutosTheme.typography.headlineMedium.color
    ) {
      Icon(
        AutosTheme.iconography.search.asImageVector,
        contentDescription = stringResource(R.string.feature_search),
        Modifier.size(64.dp)
      )

      Text(
        stringResource(R.string.feature_search_no_results_found),
        style = AutosTheme.typography.headlineMedium
      )
    }
  }
}

@Composable
@MultiThemePreview
private fun LoadingSearchPreview() {
  AutosTheme { Search(query = "", onQueryChange = {}, onBackwardsNavigation = {}) }
}

@Composable
@MultiThemePreview
private fun EmptyResultsPreview() {
  AutosTheme {
    Search(
      query = "${ProfileSearchResult.sample.account}",
      onQueryChange = {},
      results = emptyList(),
      onNavigateToProfileDetails = {},
      onBackwardsNavigation = {}
    )
  }
}

@Composable
@MultiThemePreview
private fun LoadedSearchPreview() {
  AutosTheme {
    Search(
      query = "",
      onQueryChange = {},
      listOf(ProfileSearchResult.sample),
      onNavigateToProfileDetails = {},
      onBackwardsNavigation = {}
    )
  }
}
