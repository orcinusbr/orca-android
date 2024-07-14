/*
 * Copyright © 2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.composite.searchable

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import br.com.orcinus.orca.composite.searchable.field.ResultSearchTextField
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearchResult
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.kit.action.button.SecondaryButton
import br.com.orcinus.orca.platform.autos.kit.input.text.SearchTextField
import br.com.orcinus.orca.platform.autos.kit.input.text.SearchTextFieldDefaults
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.TopAppBar
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview
import br.com.orcinus.orca.platform.focus.rememberImmediateFocusRequester
import com.jeanbarrossilva.loadable.list.ListLoadable

/**
 * Layout whose [content] can be replaced by a [SearchTextField].
 *
 * @param modifier [Modifier] applied to the underlying [Surface].
 * @param query Content to be looked up.
 * @param onQueryChange Lambda invoked whenever the [query] changes.
 * @param content Content able to be switched by a [SearchTextField] when it is requested to be
 *   shown through the provided [SearchableScope].
 */
@Composable
@VisibleForTesting
fun Searchable(
  modifier: Modifier = Modifier,
  query: String = "",
  onQueryChange: (query: String) -> Unit = {},
  content: SearchableScope.() -> Unit
) {
  Searchable(query, onQueryChange, ListLoadable.Empty(), modifier, content)
}

/**
 * Layout whose [content] can be replaced by a [SearchTextField].
 *
 * @param query Content to be looked up.
 * @param onQueryChange Lambda invoked whenever the [query] changes.
 * @param profileSearchResultsLoadable Information regarding [Profile]s found by the [query].
 * @param modifier [Modifier] applied to the underlying [Surface].
 * @param content Content able to be switched by a [SearchTextField] when it is requested to be
 *   shown through the provided [SearchableScope].
 */
@Composable
fun Searchable(
  query: String,
  onQueryChange: (query: String) -> Unit,
  profileSearchResultsLoadable: ListLoadable<ProfileSearchResult>,
  modifier: Modifier = Modifier,
  content: SearchableScope.() -> Unit
) {
  Surface(modifier, color = AutosTheme.colors.background.container.asColor) {
    val scope = remember { SearchableScope().apply(content) }
    val isSearching by remember(scope) { derivedStateOf(scope::isSearching) }
    val isResultable by
      remember(isSearching, profileSearchResultsLoadable) {
        derivedStateOf { isSearching && profileSearchResultsLoadable is ListLoadable.Populated }
      }

    LazyColumn {
      @OptIn(ExperimentalFoundationApi::class)
      stickyHeader {
        AnimatedContent(
          targetState = isSearching,
          transitionSpec = {
            slideInVertically(
              tween(durationMillis = 128, delayMillis = if (targetState) 16 else 0)
            ) {
              if (targetState) it / 2 else -it / 4
            } + fadeIn(tween(durationMillis = 128)) togetherWith
              slideOutVertically() + fadeOut(tween(durationMillis = 32)) using
              SizeTransform(clip = false)
          },
          label = "Searchable"
        ) { isSearching ->
          if (isSearching) {
            Box(Modifier.padding(SearchTextFieldDefaults.spacing).statusBarsPadding()) {
              ResultSearchTextField(
                query,
                onQueryChange,
                onDismissal = scope::dismiss,
                profileSearchResultsLoadable,
                Modifier.focusRequester(rememberImmediateFocusRequester()).fillMaxWidth()
              )
            }
          } else {
            scope.content?.invoke()
          }
        }
      }

      if (isResultable) {
        item { HorizontalDivider() }
      }
    }
  }
}

@Composable
@MultiThemePreview
private fun SearchablePreview() {
  AutosTheme {
    Searchable {
      content {
        @OptIn(ExperimentalMaterial3Api::class)
        TopAppBar(
          title = { Text("Content") },
          actions = {
            if (!isSearching) {
              SecondaryButton(onClick = ::show) { Text("Show") }
            }
          }
        )
      }
    }
  }
}
