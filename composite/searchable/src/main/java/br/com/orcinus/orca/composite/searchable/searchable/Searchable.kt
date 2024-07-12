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

package br.com.orcinus.orca.composite.searchable.searchable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.constraintlayout.compose.Dimension
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.kit.action.button.SecondaryButton
import br.com.orcinus.orca.platform.autos.kit.action.button.icon.HoverableIconButtonDefaults
import br.com.orcinus.orca.platform.autos.kit.input.text.search.SearchTextField
import br.com.orcinus.orca.platform.autos.kit.input.text.search.SearchTextFieldDefaults
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.TopAppBar
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview
import br.com.orcinus.orca.platform.focus.rememberImmediateFocusRequester

/**
 * [SearchableScope] holding a [State] whose value is toggled whenever the [SearchTextField] is
 * shown or dismissed.
 *
 * @see isSearching
 */
private class CapturingSearchableScope : SearchableScope() {
  /** Whether the [SearchTextField] is currently being shown. */
  var isSearching by mutableStateOf(false)
    private set

  override fun show() {
    isSearching = true
  }

  override fun dismiss() {
    isSearching = false
  }
}

/**
 * Layout whose [content] can be replaced by a [SearchTextField].
 *
 * @param query Content to be looked up.
 * @param onQueryChange Lambda invoked whenever the [query] changes.
 * @param modifier [Modifier] applied to the underlying [Surface].
 * @param content Content able to be switched by a [SearchTextField] when it is requested to be
 *   shown through the provided [SearchableScope].
 */
@Composable
fun Searchable(
  query: String,
  onQueryChange: (query: String) -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable SearchableScope.() -> Unit
) {
  Surface(modifier, color = AutosTheme.colors.background.container.asColor) {
    BoxWithConstraints {
      val scope = remember(::CapturingSearchableScope)

      AnimatedContent(
        targetState = scope.isSearching,
        transitionSpec = {
          slideInVertically(tween(durationMillis = 128, delayMillis = if (targetState) 16 else 0)) {
            if (targetState) it / 2 else -it / 4
          } + fadeIn(tween(durationMillis = 128)) togetherWith
            slideOutVertically() + fadeOut(tween(durationMillis = 32)) using
            SizeTransform(clip = false)
        },
        label = "Searchable"
      ) { isSearching ->
        if (isSearching) {
          Dismissible(
            onDismissal = scope::dismiss,
            Modifier.padding(SearchTextFieldDefaults.spacing).statusBarsPadding().width(maxWidth)
          ) { anchor ->
            val focusRequester = rememberImmediateFocusRequester()

            SearchTextField(
              query,
              onQueryChange,
              Modifier.focusRequester(focusRequester).constrainAs(anchor) {
                width = Dimension.fillToConstraints
                centerHorizontallyTo(parent)
              },
              contentPadding =
                PaddingValues(
                  end = HoverableIconButtonDefaults.Size.width + SearchTextFieldDefaults.spacing
                )
            )
          }
        } else {
          scope.content()
        }
      }
    }
  }
}

@Composable
@MultiThemePreview
private fun SearchablePreview() {
  AutosTheme {
    Searchable(query = "", onQueryChange = {}) {
      @OptIn(ExperimentalMaterial3Api::class)
      TopAppBar(
        title = { Text("Content") },
        actions = {
          val isNotSearching by remember {
            derivedStateOf { !(this@Searchable as CapturingSearchableScope).isSearching }
          }

          if (isNotSearching) {
            SecondaryButton(onClick = ::show) { Text("Show") }
          }
        }
      )
    }
  }
}
