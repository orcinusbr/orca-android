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

package br.com.orcinus.orca.composite.timeline.search

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import br.com.orcinus.orca.composite.timeline.search.content.SearchableContentScope
import br.com.orcinus.orca.composite.timeline.search.content.SearchableReplacementScope
import br.com.orcinus.orca.composite.timeline.search.field.ResultSearchTextField
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.kit.action.button.icon.HoverableIconButton
import br.com.orcinus.orca.platform.autos.kit.input.text.SearchTextField
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.TopAppBar
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.`if`
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview

/** Tag that identifies a [Searchable]'s content for testing purposes. */
@VisibleForTesting internal const val ContentTag = "searchable-content"

/**
 * Layout that can replace a slot by a [SearchTextField].
 *
 * @param modifier [Modifier] applied to the underlying [Box].
 * @param content Content to be shown.
 */
@Composable
fun Searchable(modifier: Modifier = Modifier, content: SearchableContentScope.() -> Unit) {
  BoxWithConstraints(modifier.testTag(ContentTag)) {
    val isReplaceableComposedState = remember { mutableStateOf(false) }
    val replacementScope =
      remember(isReplaceableComposedState) {
        SearchableReplacementScope(isReplaceableComposedState)
      }
    remember(replacementScope, content) {
        SearchableContentScope(replacementScope, isReplaceableComposedState).apply(content)
      }
      .content
      ?.invoke()
    Scrim(replacementScope)
  }
}

/**
 * Dark overlay for contrasting the [ResultSearchTextField] with the content that is laid out behind
 * it.
 *
 * @param replacementScope [SearchableReplacementScope] of the [Searchable].
 * @param modifier [Modifier] to be applied to the underlying [Canvas].
 */
@Composable
private fun BoxScope.Scrim(
  replacementScope: SearchableReplacementScope,
  modifier: Modifier = Modifier
) {
  val isSearching by remember(replacementScope) { derivedStateOf(replacementScope::isSearching) }
  val alpha by animateFloatAsState(if (isSearching) .05f else 0f, label = "Scrim alpha")
  val color = remember(alpha) { Color.Black.copy(alpha = alpha) }

  Canvas(
    modifier
      .`if`(isSearching) {
        clickable(
          interactionSource = remember(::MutableInteractionSource),
          indication = null,
          onClick = replacementScope::dismiss
        )
      }
      .matchParentSize()
  ) {
    drawRect(color)
  }
}

/** Preview of a [Searchable]. */
@Composable
@MultiThemePreview
private fun SearchablePreview() {
  AutosTheme {
    Searchable {
      content {
        Replaceable {
          @OptIn(ExperimentalMaterial3Api::class)
          TopAppBar(
            title = { Text("Main content") },
            actions = {
              HoverableIconButton(onClick = ::show) {
                Icon(
                  AutosTheme.iconography.search.asImageVector,
                  stringResource(
                    br.com.orcinus.orca.platform.autos.R.string
                      .platform_autos_search_content_description
                  )
                )
              }
            }
          )
        }
      }
    }
  }
}
