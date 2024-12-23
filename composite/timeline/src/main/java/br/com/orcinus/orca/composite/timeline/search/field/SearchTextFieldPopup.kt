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

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Popup
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
import br.com.orcinus.orca.platform.focus.rememberImmediateFocusRequester
import br.com.orcinus.orca.std.image.compose.SomeComposableImageLoader
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.loadable.list.serializableListOf
import com.jeanbarrossilva.loadable.placeholder.MediumTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.SmallTextualPlaceholder
import java.lang.ref.WeakReference

/** Duration in milliseconds of the appearance animation for results. */
private const val ResultAppearanceAnimationDurationInMilliseconds = 56

/** Duration in milliseconds of the animation of a [ResultSearchTextField]'s bottom corners. */
private const val BottomRadiusAnimationDuration =
  ResultAppearanceAnimationDurationInMilliseconds / 2

/** Default lambda which is a no-op for when a [ResultSearchTextField] query changes. */
private val NoOpOnQueryChange = { _: String -> }

/** Default [ListLoadable] of a [ResultSearchTextField]'s query results; an empty one. */
private val EmptyResultsLoadable: ListLoadable<ProfileSearchResult> = ListLoadable.Empty()

/** Default lambda which is a no-op for when a [ResultSearchTextField] is dismissed. */
private val NoOpOnDismissal = {}

/** Tag that identifies a [ResultSearchTextField]'s [ResultCard] for testing purposes. */
@InternalTimelineApi const val ResultCardTag = "result-search-text-field-result-card"

/** Tag that identifies a [ResultSearchTextField]'s "dismiss" button for testing purposes. */
@InternalTimelineApi const val DismissButtonTag = "result-search-text-field-dismiss-button"

/** Tag that identifies a [ResultSearchTextField]'s divider for testing purposes. */
@InternalTimelineApi const val DividerTag = "result-search-text-field-divider"

/**
 * Overlay of a [ResultSearchTextField] that can be composed. Acts as an alternative to a [Popup],
 * since it was the solution adopted by Orca prior to 0.3.2 and had issues such as inability to have
 * both focusability and dismissibility via an outside click, and negative
 * text-editing-decorators-Y-offsetting (see
 * [#378](https://github.com/orcinusbr/orca-android/pull/378)).
 *
 * @property contextRef [WeakReference] to the [Context] in which the [delegate] is to be displayed.
 * @property hostViewTreeOwner Owner by which the tree of the [View] that hosts the
 *   [ResultSearchTextField] is owned.
 */
@Stable
private class SearchTextFieldPopup(
  private val contextRef: WeakReference<Context>,
  private val hostViewTreeOwner: ViewTreeOwner?
) {
  /** [Modifier] applied to the [ResultSearchTextField]. */
  private var modifier by mutableStateOf<Modifier>(Modifier)

  /** Content being looked up in the [ResultSearchTextField]. */
  private var query by mutableStateOf("")

  /** Lambda invoked whenever the query changes in the [ResultSearchTextField]. */
  private var onQueryChange by mutableStateOf(NoOpOnQueryChange)

  /** [Profile] results found for the [query] in the [ResultSearchTextField]. */
  private var resultsLoadable by mutableStateOf(EmptyResultsLoadable)

  /**
   * Listener that gets notified after the [delegate] is dismissed.
   *
   * @see Dialog.dismiss
   * @see dismiss
   * @see setOnDidDismissListener
   */
  private var onDidDismissListener: (() -> Unit)? = null

  /**
   * [Dialog] by which a [ResultSearchTextField] is displayed; `null` if the [context] has been
   * garbage-collected by the time it is instantiated.
   */
  private val delegate by lazy {
    context?.let {
      Dialog(it, br.com.orcinus.orca.platform.autos.R.style.Theme_Autos).apply {
        /*
         * setCanceledOnTouchOutside(true) requires this delegate's window to wrap its content,
         * since it initially matches its parent's — decor view's — size by default and, thus, no
         * interactions are considered external to it.
         */
        window?.setLayout(
          /* width = */ WindowManager.LayoutParams.MATCH_PARENT,
          /* height = */ WindowManager.LayoutParams.WRAP_CONTENT
        )

        window?.attributes?.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
        window?.setBackgroundDrawable(transparentDrawable)
        setCanceledOnTouchOutside(true)
        setOnDismissListener { onDidDismissListener?.invoke() }
      }
    }
  }

  /** [Context] referenced by the [contextRef]. */
  private inline val context
    get() = contextRef.get()

  /**
   * Shows this popup when it enters composition and dismisses it when it is decomposed.
   *
   * @param query Content to be looked up.
   * @param onQueryChange Lambda invoked whenever the [query] changes.
   * @param resultsLoadable [Profile] results found for [query].
   * @param modifier [Modifier] to be applied to the [ResultSearchTextField].
   * @throws IllegalStateException If it is already composed. Simultaneous compositions cannot occur
   *   because the given parameters are observed, and changes to them trigger an update to their
   *   single, equivalent internal values. Parallel [Content]s could introduce inconsistent
   *   renderings and callback calls on both composables, given that their state would be shared.
   */
  @Composable
  @Throws(IllegalStateException::class)
  fun Content(
    query: String,
    onQueryChange: (query: String) -> Unit,
    resultsLoadable: ListLoadable<ProfileSearchResult>,
    modifier: Modifier = Modifier
  ) {
    SimultaneousCompositionProhibitionEffect()
    HostViewRecompositionEffect(modifier, query, onQueryChange, resultsLoadable)
    AppearanceEffect()
  }

  /**
   * Schedules the execution of an action for after the popup has been dismissed. Ultimately, allows
   * the standalone composable to replace the current listener by another one upon a recomposition
   * due to the previously set `onDismissal` lambda having changed, and remove it when it leaves
   * composition.
   *
   * @param onDidDismissListener Listener to be notified of dismissals, or `null` for removing the
   *   previously defined one.
   */
  fun setOnDidDismissListener(onDidDismissListener: (() -> Unit)?) {
    delegate?.setOnDismissListener(onDidDismissListener?.let { { it() } })
  }

  /**
   * Creates a [ComposeView] by which a [ResultSearchTextField] is hosted, whose tree ownership is
   * configured upon a request to show this popup and deconfigured whenever it is dismissed — both
   * respectively triggered by calls to [show] and [dismiss].
   *
   * @return The host [View], or `null` in case this method gets called after the [context] has been
   *   garbage-collected.
   * @see hostViewTreeOwner
   */
  private fun createHostView() =
    context?.let {
      ComposeView(it).apply {
        setContent {
          AutosTheme {
            Box(
              Modifier.padding(
                start = SearchTextFieldDefaults.spacing,
                top = SearchTextFieldDefaults.spacing,
                end = SearchTextFieldDefaults.spacing,

                /*
                 * Dimensions of the shadow cast by the text field are disregarded by the view when
                 * it is measured; without the padding below, it gets clipped. Calling
                 * setClipChildren(false) is futile in this case, given that the text field itself
                 * is a composable rather than a child view.
                 */
                bottom = SearchTextFieldDefaults.Elevation
              )
            ) {
              ResultSearchTextField(
                query,
                onQueryChange,
                onDismissal = ::dismiss,
                resultsLoadable,
                modifier.focusRequester(rememberImmediateFocusRequester()).fillMaxWidth()
              )
            }
          }
        }
      }
    }

  /**
   * Effect that runs once upon a composition and ensures that this popup is not already displayed;
   * it being so implies a request for a parallel rendering of it, which outreaches its intended
   * capacity of one appearance at a time (given that it is represented by a single [Dialog]).
   *
   * @throws IllegalStateException If this popup is already composed.
   * @see delegate
   */
  @Composable
  @Throws(IllegalStateException::class)
  private fun SimultaneousCompositionProhibitionEffect() =
    DisposableEffect(Unit) {
      onDispose {
        delegate?.let {
          check(!it.isShowing) {
            "Cannot perform simultaneous compositions of a search text field popup!"
          }
        }
      }
    }

  /**
   * Effect that triggers recompositions on a host [View] by assigning the given parameters to this
   * popup's [State]-based properties. Each of them is then reset upon a decomposition, with a
   * default, empty value.
   *
   * @param modifier [Modifier] to be applied to the [ResultSearchTextField].
   * @param query Content to be looked up.
   * @param onQueryChange Lambda invoked whenever the [query] changes.
   * @param resultsLoadable [Profile] results found for [query].
   * @see createHostView
   * @see SearchTextFieldPopup.modifier
   * @see SearchTextFieldPopup.query
   * @see SearchTextFieldPopup.onQueryChange
   * @see SearchTextFieldPopup.resultsLoadable
   */
  @Composable
  private fun HostViewRecompositionEffect(
    modifier: Modifier,
    query: String,
    onQueryChange: (query: String) -> Unit,
    resultsLoadable: ListLoadable<ProfileSearchResult>
  ) {
    DisposableEffect(modifier) {
      this@SearchTextFieldPopup.modifier = modifier
      onDispose { this@SearchTextFieldPopup.modifier = Modifier }
    }

    DisposableEffect(query) {
      this@SearchTextFieldPopup.query = query
      onDispose { this@SearchTextFieldPopup.query = "" }
    }

    DisposableEffect(onQueryChange) {
      this@SearchTextFieldPopup.onQueryChange = onQueryChange
      onDispose { this@SearchTextFieldPopup.onQueryChange = NoOpOnQueryChange }
    }

    DisposableEffect(resultsLoadable) {
      this@SearchTextFieldPopup.resultsLoadable = resultsLoadable
      onDispose { this@SearchTextFieldPopup.resultsLoadable = EmptyResultsLoadable }
    }
  }

  /**
   * Effect that shows and dismisses this popup.
   *
   * @see show
   * @see dismiss
   */
  @Composable
  private fun AppearanceEffect() =
    DisposableEffect(Unit) {
      show()
      onDispose(::dismiss)
    }

  /** Shows this popup, by which the [ResultSearchTextField] is displayed. */
  private fun show() {
    val delegate = delegate
    val hostView = createHostView()
    val hostViewTreeOwner = hostViewTreeOwner
    if (delegate != null && hostView != null && hostViewTreeOwner != null) {
      hostViewTreeOwner.own(hostView)
      delegate.setContentView(hostView)
      delegate.show()
    }
  }

  /** Dismisses this popup, by which the [ResultSearchTextField] is displayed. */
  private fun dismiss() {
    delegate?.dismiss()
  }

  private companion object {
    /**
     * [ColorDrawable] defined as the [delegate]'s [Window] background for making it transparent.
     */
    @JvmStatic private val transparentDrawable = ColorDrawable(Color.TRANSPARENT)
  }
}

/**
 * Popup by which a [SearchTextField] that presents results for the [query] is displayed.
 *
 * This overload is stateless by default and for testing purposes only.
 *
 * @param modifier [Modifier] to be applied to the [ResultSearchTextField].
 * @param query Content to be looked up.
 * @param onQueryChange Lambda invoked whenever the [query] changes.
 * @param resultsLoadable [Profile] results found for [query].
 * @param onDismissal Operation performed whenever this popup is dismissed.
 */
@Composable
@VisibleForTesting
fun SearchTextFieldPopup(
  modifier: Modifier = Modifier,
  query: String = "",
  onQueryChange: (query: String) -> Unit = NoOpOnQueryChange,
  resultsLoadable: ListLoadable<ProfileSearchResult> = EmptyResultsLoadable,
  onDismissal: () -> Unit = NoOpOnDismissal
) = SearchTextFieldPopup(query, onQueryChange, resultsLoadable, onDismissal, modifier)

/**
 * Popup by which a [SearchTextField] that presents results for the [query] is displayed.
 *
 * @param query Content to be looked up.
 * @param onQueryChange Lambda invoked whenever the [query] changes.
 * @param resultsLoadable [Profile] results found for the [query].
 * @param onDismissal Operation performed whenever this popup is dismissed.
 * @param modifier [Modifier] to be applied to the [SearchTextField].
 */
@Composable
fun SearchTextFieldPopup(
  query: String,
  onQueryChange: (query: String) -> Unit,
  resultsLoadable: ListLoadable<ProfileSearchResult>,
  onDismissal: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val view = LocalView.current
  val popup =
    remember(context, view) {
      SearchTextFieldPopup(WeakReference(context), hostViewTreeOwner = ViewTreeOwner.of(view))
    }

  DisposableEffect(onDismissal) {
    popup.setOnDidDismissListener(onDismissal)
    onDispose { popup.setOnDidDismissListener(null) }
  }

  popup.Content(query, onQueryChange, resultsLoadable, modifier)
}

/**
 * [DismissibleSearchTextField] that presents results for the query.
 *
 * This overload is stateless by default and is intended for previewing only.
 *
 * @param modifier [Modifier] to be applied to the [SearchTextField].
 * @param query Content to be looked up.
 * @param onQueryChange Lambda invoked whenever the [query] changes.
 * @param onDismissal Callback called when dismissal is requested.
 * @param resultsLoadable [Profile] results found for [query].
 */
@Composable
@InternalTimelineApi
@VisibleForTesting
private fun ResultSearchTextField(
  modifier: Modifier = Modifier,
  query: String = "",
  onQueryChange: (query: String) -> Unit = NoOpOnQueryChange,
  onDismissal: () -> Unit = NoOpOnDismissal,
  resultsLoadable: ListLoadable<ProfileSearchResult> = EmptyResultsLoadable
) {
  ResultSearchTextField(query, onQueryChange, onDismissal, resultsLoadable, modifier)
}

/**
 * [DismissibleSearchTextField] that presents results for the [query].
 *
 * @param query Content to be looked up.
 * @param onQueryChange Lambda invoked whenever the [query] changes.
 * @param onDismissal Callback called when dismissal is requested.
 * @param resultsLoadable [Profile] results found for [query].
 * @param modifier [Modifier] to be applied to the [SearchTextField].
 */
@Composable
private fun ResultSearchTextField(
  query: String,
  onQueryChange: (query: String) -> Unit,
  onDismissal: () -> Unit,
  resultsLoadable: ListLoadable<ProfileSearchResult>,
  modifier: Modifier = Modifier
) {
  val defaultElevation = SearchTextFieldDefaults.Elevation
  val containsResults by
    remember(resultsLoadable) { mutableStateOf(resultsLoadable is ListLoadable.Populated) }
  val layoutElevation by
    remember(containsResults) { derivedStateOf { if (containsResults) defaultElevation else 0.dp } }

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
        animationSpec = tween(BottomRadiusAnimationDuration),
        label = "Bottom end radius"
      )
    val searchTextFieldBottomStartRadius by
      animateDpAsState(
        if (containsResults) {
          0.dp
        } else {
          SearchTextFieldDefaults.shape.bottomStart.toDp(searchTextFieldSize, density)
        },
        animationSpec = tween(BottomRadiusAnimationDuration),
        label = "Bottom start radius"
      )
    val searchTextFieldElevation by
      remember(containsResults) {
        derivedStateOf { if (containsResults) 0.dp else defaultElevation }
      }

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
    modifier.testTag(ResultCardTag),
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
        ProvideTextStyle(AutosTheme.typography.titleMedium, name)
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
