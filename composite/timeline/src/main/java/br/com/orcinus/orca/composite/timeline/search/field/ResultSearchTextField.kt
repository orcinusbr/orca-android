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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import br.com.orcinus.orca.composite.timeline.InternalTimelineApi
import br.com.orcinus.orca.composite.timeline.R
import br.com.orcinus.orca.composite.timeline.avatar.SmallAvatar
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearchResult
import br.com.orcinus.orca.core.sample.feed.profile.account.sample
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.kit.action.button.icon.HoverableIconButton
import br.com.orcinus.orca.platform.autos.kit.action.button.icon.HoverableIconButtonDefaults
import br.com.orcinus.orca.platform.autos.kit.bottom
import br.com.orcinus.orca.platform.autos.kit.input.text.SearchTextField
import br.com.orcinus.orca.platform.autos.kit.input.text.SearchTextFieldDefaults
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.`if`
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview
import br.com.orcinus.orca.platform.core.sample
import br.com.orcinus.orca.std.image.compose.SomeComposableImageLoader
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.loadable.list.serializableListOf
import com.jeanbarrossilva.loadable.placeholder.MediumTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.SmallTextualPlaceholder

/** Duration in milliseconds of the appearance animation for results. */
private const val ResultAppearanceAnimationDurationInMilliseconds = 56

/** Tag that identifies a [ResultSearchTextField]'s "dismiss" button for testing purposes. */
@InternalTimelineApi const val DismissButtonTag = "result-search-text-field-dismiss-button"

/** Tag that identifies a [ResultSearchTextField]'s divider for testing purposes. */
@InternalTimelineApi const val DividerTag = "result-search-text-field-divider"

/**
 * [SearchTextField] that presents results for the query.
 *
 * This overload is stateless by default and is intended for previewing and testing purposes only.
 *
 * @param modifier [Modifier] to be applied to the [SearchTextField].
 * @param query Content to be looked up.
 * @param onQueryChange Lambda invoked whenever the [query] changes.
 * @param onDismissal Callback called when dismissal is requested.
 * @param resultsLoadable [Profile] results found by the [query].
 */
@Composable
@InternalTimelineApi
@VisibleForTesting
fun ResultSearchTextField(
  modifier: Modifier = Modifier,
  query: String = "",
  onQueryChange: (query: String) -> Unit = {},
  onDismissal: () -> Unit = {},
  resultsLoadable: ListLoadable<ProfileSearchResult> = ListLoadable.Empty()
) {
  ResultSearchTextField(query, onQueryChange, onDismissal, resultsLoadable, modifier)
}

/**
 * [SearchTextField] that presents results for the [query].
 *
 * @param query Content to be looked up.
 * @param onQueryChange Lambda invoked whenever the [query] changes.
 * @param onDismissal Callback called when dismissal is requested.
 * @param resultsLoadable [Profile] results found by the [query].
 * @param modifier [Modifier] to be applied to the [SearchTextField].
 */
@Composable
internal fun ResultSearchTextField(
  query: String,
  onQueryChange: (query: String) -> Unit,
  onDismissal: () -> Unit,
  resultsLoadable: ListLoadable<ProfileSearchResult>,
  modifier: Modifier = Modifier
) {
  val containsResults = remember(resultsLoadable) { resultsLoadable is ListLoadable.Populated }
  val layoutElevation by
    animateDpAsState(
      if (containsResults) SearchTextFieldDefaults.Elevation else 0.dp,
      animationSpec = tween(delayMillis = ResultAppearanceAnimationDurationInMilliseconds * 8),
      label = "Layout elevation"
    )

  ConstraintLayout(
    Modifier.shadow(layoutElevation, SearchTextFieldDefaults.shape).`if`(containsResults) {
      clip(SearchTextFieldDefaults.shape)
    }
  ) {
    val density = LocalDensity.current
    val (searchTextFieldRef, resultsRef) = createRefs()
    var searchTextFieldSize by remember { mutableStateOf(Size.Unspecified) }
    val searchTextFieldBottomEndRadius by
      animateDpAsState(
        if (containsResults) {
          0.dp
        } else {
          SearchTextFieldDefaults.shape.bottomEnd.toDp(searchTextFieldSize, density)
        },
        animationSpec = tween(ResultAppearanceAnimationDurationInMilliseconds),
        label = "Bottom end radius"
      )
    val searchTextFieldBottomStartRadius by
      animateDpAsState(
        if (containsResults) {
          0.dp
        } else {
          SearchTextFieldDefaults.shape.bottomStart.toDp(searchTextFieldSize, density)
        },
        animationSpec = tween(ResultAppearanceAnimationDurationInMilliseconds),
        label = "Bottom start radius"
      )
    val searchTextFieldElevation by
      animateDpAsState(
        if (containsResults) 0.dp else SearchTextFieldDefaults.Elevation,
        label = "Search text field elevation"
      )

    DismissibleSearchTextField(
      searchTextFieldRef,
      query,
      onQueryChange,
      SearchTextFieldDefaults.shape.copy(
        bottomEnd = CornerSize(searchTextFieldBottomEndRadius),
        bottomStart = CornerSize(searchTextFieldBottomStartRadius)
      ),
      searchTextFieldElevation,
      resultsLoadable is ListLoadable.Loading,
      onDismissal,
      modifier
        .onSizeChanged { searchTextFieldSize = it.toSize() }
        .constrainAs(searchTextFieldRef) {}
    )

    AnimatedVisibility(
      visible = containsResults,
      Modifier.constrainAs(resultsRef) {
          width = Dimension.fillToConstraints
          centerHorizontallyTo(parent)
          top.linkTo(searchTextFieldRef.bottom)
        }
        .zIndex(-1f),
      enter = slideInVertically(tween(ResultAppearanceAnimationDurationInMilliseconds)),
      exit = slideOutVertically()
    ) {
      HorizontalDivider(Modifier.testTag(DividerTag))

      LazyColumn(
        Modifier.constrainAs(createRef()) {
          width = Dimension.fillToConstraints
          centerHorizontallyTo(parent)
          top.linkTo(resultsRef.bottom)
        }
      ) {
        resultsLoadable.ifPopulated {
          itemsIndexed(this) { index, result ->
            ResultCard(result, isLastOne = index == lastIndex, Modifier.fillMaxWidth())
          }
        }
      }
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
 * @param elevation Amount in [Dp] by which it is elevated.
 * @param isLoading Whether it is to be put in a loading state. Ultimately, is reflected on the UI
 *   by having the "search" icon replaced by a [CircularProgressIndicator] that spins indefinitely.
 * @param onDismissal Callback called when dismissal is requested.
 * @param modifier [Modifier] to be applied to the [SearchTextField].
 */
@Composable
private fun ConstraintLayoutScope.DismissibleSearchTextField(
  ref: ConstrainedLayoutReference,
  query: String,
  onQueryChange: (query: String) -> Unit,
  shape: Shape,
  elevation: Dp,
  isLoading: Boolean,
  onDismissal: () -> Unit,
  modifier: Modifier = Modifier
) {
  SearchTextField(
    query,
    onQueryChange,
    isLoading,
    modifier.constrainAs(ref) {},
    shape,
    elevation,
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

/**
 * [Card] of a loading [ProfileSearchResult].
 *
 * @param isLastOne Whether this [ResultCard] is for the last found result.
 * @param modifier [Modifier] to be applied to the underlying [Card].
 */
@Composable
private fun ResultCard(isLastOne: Boolean, modifier: Modifier = Modifier) {
  ResultCard(
    avatar = { SmallAvatar() },
    name = { MediumTextualPlaceholder() },
    account = { SmallTextualPlaceholder() },
    onClick = {},
    isLastOne,
    modifier
  )
}

/**
 * [Card] of a loaded [ProfileSearchResult].
 *
 * @param result [ProfileSearchResult] whose information is to be displayed.
 * @param isLastOne Whether this [ResultCard] is for the last found result.
 * @param modifier [Modifier] to be applied to the underlying [Card].
 */
@Composable
private fun ResultCard(
  result: ProfileSearchResult,
  isLastOne: Boolean,
  modifier: Modifier = Modifier
) {
  ResultCard(
    avatar = { SmallAvatar(result.avatarLoader as SomeComposableImageLoader, result.name) },
    name = { Text(result.name) },
    account = { Text("${result.account}") },
    onClick = {},
    isLastOne,
    modifier
  )
}

/**
 * [Card] that is a skeleton for a search result.
 *
 * @param avatar Slot for the picture of the found [Profile].
 * @param name Slot for the name of the [Profile].
 * @param account Slot for the account of the [Profile].
 * @param onClick Action to be executed when it is clicked.
 * @param isLastOne Whether this [ResultCard] is for the last found result.
 * @param modifier [Modifier] to be applied to the underlying [Card].
 */
@Composable
private fun ResultCard(
  avatar: @Composable () -> Unit,
  name: @Composable () -> Unit,
  account: @Composable () -> Unit,
  onClick: () -> Unit,
  isLastOne: Boolean,
  modifier: Modifier = Modifier
) {
  Card(
    onClick,
    modifier,
    shape = if (isLastOne) SearchTextFieldDefaults.shape.bottom else RectangleShape,
    colors = CardDefaults.cardColors(containerColor = SearchTextFieldDefaults.containerColor),
    elevation =
      CardDefaults.cardElevation(
        defaultElevation = 0.dp,
        pressedElevation = 0.dp,
        focusedElevation = 0.dp,
        hoveredElevation = 0.dp,
        draggedElevation = 0.dp,
        disabledElevation = 0.dp
      )
  ) {
    Row(
      Modifier.padding(SearchTextFieldDefaults.spacing),
      Arrangement.spacedBy(SearchTextFieldDefaults.spacing),
      Alignment.CenterVertically
    ) {
      avatar()

      Column(verticalArrangement = Arrangement.spacedBy(AutosTheme.spacings.extraSmall.dp)) {
        ProvideTextStyle(AutosTheme.typography.labelMedium, name)
        ProvideTextStyle(AutosTheme.typography.labelSmall, account)
      }
    }
  }
}

/** Preview of a [ResultSearchTextField] with loading results. */
@Composable
@MultiThemePreview
private fun ResultSearchTextFieldLoadingResultsPreview() {
  AutosTheme { ResultSearchTextField(resultsLoadable = ListLoadable.Loading()) }
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
      resultsLoadable = ListLoadable.Populated(serializableListOf(ProfileSearchResult.sample))
    )
  }
}
