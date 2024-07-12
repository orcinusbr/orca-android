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

package br.com.orcinus.orca.platform.autos.kit.input.text.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.platform.autos.R
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.forms.asShape
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview

/** Tag that identifies a [SearchTextField] for testing purposes. */
const val SearchTextFieldTag = "search-text-field"

/** Default values used by a [SearchTextField]. */
object SearchTextFieldDefaults {
  /** Amount of [Dp]s by which a [SearchTextField] is spaced by default. */
  val spacing
    @Composable get() = AutosTheme.spacings.medium.dp
}

/**
 * Text field for searching content.
 *
 * @param query Content to be looked up.
 * @param onQueryChange Lambda invoked whenever the [query] changes.
 * @param modifier [Modifier] applied to the underlying [BasicTextField].
 * @param contentPadding [PaddingValues] by which the content of the decoration box is padded.
 */
@Composable
fun SearchTextField(
  query: String,
  onQueryChange: (query: String) -> Unit,
  modifier: Modifier = Modifier,
  contentPadding: PaddingValues = PaddingValues()
) {
  val style = LocalTextStyle.current
  val cursorBrush = remember(style) { SolidColor(style.color) }

  BasicTextField(
    query,
    onQueryChange,
    modifier.testTag(SearchTextFieldTag),
    textStyle = style,
    cursorBrush = cursorBrush
  ) {
    Surface(shape = AutosTheme.forms.large.asShape, shadowElevation = 2.dp) {
      Row(
        Modifier.padding(contentPadding).padding(SearchTextFieldDefaults.spacing),
        Arrangement.spacedBy(SearchTextFieldDefaults.spacing),
        Alignment.CenterVertically
      ) {
        Icon(
          AutosTheme.iconography.search.asImageVector,
          stringResource(R.string.platform_autos_search_content_description),
          tint = AutosTheme.colors.secondary.asColor
        )

        if (query.isEmpty()) {
          Text(
            stringResource(R.string.platform_autos_search),
            style = AutosTheme.typography.titleSmall
          )
        } else {
          it()
        }
      }
    }
  }
}

/** Preview of an empty [SearchTextField]. */
@Composable
@MultiThemePreview
private fun EmptySearchTextFieldPreview() {
  AutosTheme { SearchTextField(query = "", onQueryChange = {}) }
}

/** Preview of a populated [SearchTextField]. */
@Composable
@MultiThemePreview
private fun PopulatedSearchTextFieldPreview() {
  AutosTheme { SearchTextField(query = "Flamengo", onQueryChange = {}) }
}
