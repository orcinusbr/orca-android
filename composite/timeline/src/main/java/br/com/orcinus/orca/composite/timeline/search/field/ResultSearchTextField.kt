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

package br.com.orcinus.orca.composite.timeline.search.field

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import br.com.orcinus.orca.composite.timeline.InternalTimelineApi
import br.com.orcinus.orca.composite.timeline.R
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearchResult
import br.com.orcinus.orca.core.sample.feed.profile.account.sample
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.kit.action.button.icon.HoverableIconButton
import br.com.orcinus.orca.platform.autos.kit.action.button.icon.HoverableIconButtonDefaults
import br.com.orcinus.orca.platform.autos.kit.input.text.SearchTextField
import br.com.orcinus.orca.platform.autos.kit.input.text.SearchTextFieldDefaults
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview
import br.com.orcinus.orca.platform.core.sample
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.loadable.list.serializableListOf

/** Tag that identifies a [ResultSearchTextField]'s "dismiss" button for testing purposes. */
const val DismissButtonTag = "result-search-text-field-dismiss-button"

/** Tag that identifies a [ResultSearchTextField]'s divider for testing purposes. */
const val DividerTag = "result-search-text-field-divider-tag"

/**
 * [SearchTextField] that presents results for the [query].
 *
 * This overload is stateless by default and is intended for previewing and testing purposes only.
 *
 * @param modifier [Modifier] to be applied to the [SearchTextField].
 * @param query Content to be looked up.
 * @param onQueryChange Lambda invoked whenever the [query] changes.
 * @param onDismissal Callback called when dismissal is requested.
 * @param profileSearchResultsLoadable [Profile] results found by the [query].
 */
@Composable
@InternalTimelineApi
@VisibleForTesting
fun ResultSearchTextField(
  modifier: Modifier = Modifier,
  query: String = "",
  onQueryChange: (query: String) -> Unit = {},
  onDismissal: () -> Unit = {},
  profileSearchResultsLoadable: ListLoadable<ProfileSearchResult> = ListLoadable.Loading()
) {
  ResultSearchTextField(query, onQueryChange, onDismissal, profileSearchResultsLoadable, modifier)
}

/**
 * [SearchTextField] that presents results for the [query].
 *
 * @param query Content to be looked up.
 * @param onQueryChange Lambda invoked whenever the [query] changes.
 * @param onDismissal Callback called when dismissal is requested.
 * @param profileSearchResultsLoadable [Profile] results found by the [query].
 * @param modifier [Modifier] to be applied to the [SearchTextField].
 */
@Composable
internal fun ResultSearchTextField(
  query: String,
  onQueryChange: (query: String) -> Unit,
  onDismissal: () -> Unit,
  profileSearchResultsLoadable: ListLoadable<ProfileSearchResult>,
  modifier: Modifier = Modifier
) {
  ConstraintLayout {
    val density = LocalDensity.current
    val containsResults = profileSearchResultsLoadable is ListLoadable.Populated
    val searchTextFieldRef = createRef()
    var searchTextFieldSize by remember { mutableStateOf(Size.Unspecified) }
    val searchTextFieldShape = SearchTextFieldDefaults.shape
    val searchTextFieldBottomEndRadius by
      animateDpAsState(
        targetValue =
          if (containsResults) {
            0.dp
          } else {
            searchTextFieldShape.bottomEnd.toDp(searchTextFieldSize, density)
          },
        label = "Search text field bottom end radius"
      )
    val searchTextFieldBottomStartRadius by
      animateDpAsState(
        targetValue =
          if (containsResults) {
            0.dp
          } else {
            searchTextFieldShape.bottomStart.toDp(searchTextFieldSize, density)
          },
        label = "Search text field bottom start radius"
      )

    DismissibleSearchTextField(
      searchTextFieldRef,
      query,
      onQueryChange,
      searchTextFieldShape.copy(
        bottomEnd = CornerSize(searchTextFieldBottomEndRadius),
        bottomStart = CornerSize(searchTextFieldBottomStartRadius)
      ),
      onDismissal,
      modifier
        .onSizeChanged { searchTextFieldSize = it.toSize() }
        .constrainAs(searchTextFieldRef) {}
    )

    profileSearchResultsLoadable.ifPopulated {
      HorizontalDivider(
        Modifier.constrainAs(createRef()) {
            width = Dimension.fillToConstraints
            centerHorizontallyTo(parent)
            top.linkTo(searchTextFieldRef.bottom)
          }
          .testTag(DividerTag)
      )
    }
  }
}

/**
 * [SearchTextField] with a "dismiss" button.
 *
 * @param ref [ConstrainedLayoutReference] to the [SearchTextField].
 * @param query Content to be looked up.
 * @param onQueryChange Lambda invoked whenever the [query] changes.
 * @param shape [Shape] by which the [SearchTextField] is clipped.
 * @param onDismissal Callback called when dismissal is requested.
 * @param modifier [Modifier] to be applied to the [SearchTextField].
 */
@Composable
private fun ConstraintLayoutScope.DismissibleSearchTextField(
  ref: ConstrainedLayoutReference,
  query: String,
  onQueryChange: (query: String) -> Unit,
  shape: Shape,
  onDismissal: () -> Unit,
  modifier: Modifier = Modifier
) {
  SearchTextField(
    query,
    onQueryChange,
    modifier.constrainAs(ref) {},
    shape,
    contentPadding =
      PaddingValues(
        end = HoverableIconButtonDefaults.Size.width + SearchTextFieldDefaults.spacing * 2
      )
  )

  HoverableIconButton(
    onClick = onDismissal,
    Modifier.constrainAs(createRef()) {
        centerVerticallyTo(ref)
        end.linkTo(ref.end)
      }
      .testTag(DismissButtonTag)
  ) {
    Icon(
      AutosTheme.iconography.close.asImageVector,
      contentDescription = stringResource(R.string.composite_timeline_dismiss)
    )
  }
}

/** Preview of a [ResultSearchTextField] with loading results. */
@Composable
@MultiThemePreview
private fun ResultSearchTextFieldLoadingResultsPreview() {
  AutosTheme { ResultSearchTextField(profileSearchResultsLoadable = ListLoadable.Loading()) }
}

/** Preview of a [ResultSearchTextField] without results. */
@Composable
@MultiThemePreview
private fun ResultSearchTextFieldWithoutResultsPreview() {
  AutosTheme { ResultSearchTextField() }
}

/** Preview of a [ResultSearchTextField] with results. */
@Composable
@MultiThemePreview
private fun ResultSearchTextFieldWithResultsPreview() {
  AutosTheme {
    ResultSearchTextField(
      query = "${Account.sample.username}",
      profileSearchResultsLoadable =
        ListLoadable.Populated(serializableListOf(ProfileSearchResult.sample))
    )
  }
}
