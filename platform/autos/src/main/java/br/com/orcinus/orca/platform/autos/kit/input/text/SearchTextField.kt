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

package br.com.orcinus.orca.platform.autos.kit.input.text

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.platform.autos.InternalPlatformAutosApi
import br.com.orcinus.orca.platform.autos.R
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.forms.asShape
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.kit.action.button.icon.HoverableIconButton
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.loadable.placeholder.test.Loading

/** Tag that identifies a [SearchTextField]'s "loading" indicator for testing purposes. */
internal const val LoadingIndicatorTag = "search-text-field-loading-indicator-tag"

/** Tag that identifies a [SearchTextField]'s "search" icon for testing purposes. */
internal const val SearchIconTag = "search-text-field-search-icon"

/** Tag that identifies a [SearchTextField] for testing purposes. */
const val SearchTextFieldTag = "search-text-field"

/** Default values used by a [SearchTextField]. */
object SearchTextFieldDefaults {
  /** Size of a [SearchTextField]'s leading icon and [CircularProgressIndicator] by default. */
  val IconSize = 24.dp

  /** Amount in [Dp] by which a [SearchTextField] is elevated by default. */
  val Elevation = 2.dp

  /** [Color] by which the container of a [SearchTextField] is colored by default. */
  val containerColor
    @Composable get() = AutosTheme.colors.surface.container.asColor

  /** [Shape] of a [SearchTextField] by default. */
  val shape
    @Composable get() = AutosTheme.forms.large.asShape

  /** Amount of [Dp]s by which a [SearchTextField] is spaced by default. */
  val spacing
    @Composable get() = AutosTheme.spacings.medium.dp
}

/**
 * Text field for searching content.
 *
 * This overload is stateless by default and is intended for previewing and testing purposes only.
 *
 * @param query Content to be looked up.
 * @param onQueryChange Lambda invoked whenever the [query] changes.
 * @param isLoading Whether it is to be put in a loading state. Ultimately, is reflected on the UI
 *   by having the "search" icon replaced by a [CircularProgressIndicator] that spins indefinitely.
 * @param modifier [Modifier] applied to the underlying [BasicTextField].
 * @param shape [Shape] by which it is clipped.
 * @param elevation Amount in [Dp] by which it is elevated.
 * @param contentPadding [PaddingValues] by which the content of the decoration box is padded.
 * @param trailing [Composable] to be displayed at the horizontal end.
 */
@Composable
@InternalPlatformAutosApi
@VisibleForTesting
fun SearchTextField(
  modifier: Modifier = Modifier,
  query: String = "",
  onQueryChange: (query: String) -> Unit = {},
  shape: Shape = SearchTextFieldDefaults.shape,
  elevation: Dp = SearchTextFieldDefaults.Elevation,
  isLoading: Boolean = false,
  contentPadding: PaddingValues = PaddingValues(),
  trailing: @Composable () -> Unit = {
    if (query.isNotEmpty()) {
      HoverableIconButton(onClick = { onQueryChange("") }, Modifier.size(24.dp)) {
        Icon(AutosTheme.iconography.close.asImageVector, contentDescription = "Clear")
      }
    }
  }
) =
  SearchTextField(
    query,
    onQueryChange,
    isLoading,
    modifier,
    shape,
    elevation,
    contentPadding,
    trailing
  )

/**
 * Text field for searching content.
 *
 * @param query Content to be looked up.
 * @param onQueryChange Lambda invoked whenever the [query] changes.
 * @param isLoading Whether it is to be put in a loading state. Ultimately, is reflected on the UI
 *   by having the "search" icon replaced by a [CircularProgressIndicator] that spins indefinitely.
 * @param modifier [Modifier] applied to the underlying [BasicTextField].
 * @param shape [Shape] by which it is clipped.
 * @param elevation Amount in [Dp] by which it is elevated.
 * @param contentPadding [PaddingValues] by which the content of the decoration box is padded.
 * @param trailing [Composable] to be displayed at the horizontal end.
 */
@Composable
fun SearchTextField(
  query: String,
  onQueryChange: (query: String) -> Unit,
  isLoading: Boolean,
  modifier: Modifier = Modifier,
  shape: Shape = SearchTextFieldDefaults.shape,
  elevation: Dp = SearchTextFieldDefaults.Elevation,
  contentPadding: PaddingValues = PaddingValues(),
  trailing: @Composable () -> Unit = {}
) {
  val style = LocalTextStyle.current
  var width = remember(Dp::Unspecified)
  val cursorBrush = remember(style) { SolidColor(style.color) }

  BasicTextField(
    query,
    onQueryChange,
    modifier
      .testTag(SearchTextFieldTag)
      .semantics { set(SemanticsProperties.Loading, isLoading) }
      .layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        val widthInPixels = placeable.width
        width = widthInPixels.toDp()
        layout(widthInPixels, placeable.height) { placeable.place(x = 0, y = 0) }
      },
    textStyle = style,
    cursorBrush = cursorBrush
  ) {
    Surface(shape = shape, shadowElevation = elevation) {
      Row(
        Modifier.padding(contentPadding).padding(SearchTextFieldDefaults.spacing).width(width),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically
      ) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(SearchTextFieldDefaults.spacing),
          verticalAlignment = Alignment.CenterVertically
        ) {
          CompositionLocalProvider(LocalContentColor provides AutosTheme.colors.secondary.asColor) {
            if (isLoading) {
              CircularProgressIndicator(
                Modifier.size(SearchTextFieldDefaults.IconSize).testTag(LoadingIndicatorTag),
                color = LocalContentColor.current
              )
            } else {
              Icon(
                AutosTheme.iconography.search.asImageVector,
                stringResource(R.string.platform_autos_search_content_description),
                Modifier.testTag(SearchIconTag)
              )
            }
          }

          Box {
            query.ifEmpty {
              Text(
                stringResource(R.string.platform_autos_search),
                style = AutosTheme.typography.titleSmall
              )
            }

            it()
          }
        }

        Spacer(Modifier.width(SearchTextFieldDefaults.spacing))
        trailing()
      }
    }
  }
}

/** Preview of an empty, loading [SearchTextField]. */
@Composable
@MultiThemePreview
private fun EmptyLoadingSearchTextFieldPreview() {
  AutosTheme { SearchTextField(isLoading = true) }
}

/** Preview of an empty, loaded [SearchTextField]. */
@Composable
@MultiThemePreview
private fun EmptyLoadedSearchTextFieldPreview() {
  AutosTheme { SearchTextField() }
}

/** Preview of a populated, loading [SearchTextField]. */
@Composable
@MultiThemePreview
private fun PopulatedLoadingSearchTextFieldPreview() {
  AutosTheme { SearchTextField(query = "Flamengo", isLoading = true) }
}

/** Preview of a populated, loaded [SearchTextField]. */
@Composable
@MultiThemePreview
private fun PopulatedLoadedSearchTextFieldPreview() {
  AutosTheme { SearchTextField(query = "Flamengo") }
}
