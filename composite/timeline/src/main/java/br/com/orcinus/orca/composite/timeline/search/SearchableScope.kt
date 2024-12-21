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
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import br.com.orcinus.orca.composite.timeline.search.field.ResultSearchTextField
import br.com.orcinus.orca.composite.timeline.search.field.rememberResultSearchTextFieldPopup
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearchResult
import br.com.orcinus.orca.platform.autos.kit.input.text.SearchTextField
import br.com.orcinus.orca.platform.autos.kit.input.text.SearchTextFieldDefaults
import com.jeanbarrossilva.loadable.list.ListLoadable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Scope of a [Searchable] in which the [SearchTextField] can be either shown or dismissed.
 *
 * @property isReplaceableComposedState [MutableState] whose [Boolean] determines whether the
 *   content to be replaced by the [SearchTextField] is currently composed.
 * @property fillerColor [Color] by which the container that fills the space previously occupied by
 *   the content replaced by the [SearchTextField] is colored.
 */
class SearchableScope
internal constructor(
  private val isReplaceableComposedState: MutableState<Boolean>,
  private val fillerColor: Color
) {
  /** Maximum [Size] that the [Filler] has had up until now. */
  private var fillerPeakSize = Size.Unspecified

  /** [Animatable] by which the height of the [ResultSearchTextField] is held and animated. */
  private val resultSearchTextFieldHeightAnimatable =
    Animatable(initialValue = 0.dp, Dp.VectorConverter)

  /** Whether the [ResultSearchTextField] is currently being shown. */
  var isSearching by mutableStateOf(false)
    private set

  /** Whether the search currently being performed has yielded any results. */
  var containsSearchResults by mutableStateOf(false)
    private set

  /** Height of the [SearchTextField], or zeroed in case search isn't being performed. */
  val resultSearchTextFieldHeight by resultSearchTextFieldHeightAnimatable.asState()

  /** Alias for [resultSearchTextFieldHeight]. */
  @Deprecated(
    "Renamed to \"resultSearchTextFieldHeight\".",
    ReplaceWith(
      "resultSearchTextFieldHeight",
      imports = ["br.com.orcinus.orca.composite.timeline.search.SearchableScope"]
    )
  )
  inline val searchTextFieldLayoutHeight
    get() = resultSearchTextFieldHeight

  /**
   * [State] with the radius of the blur that should be applied to the content.
   *
   * @see BlurRadii
   */
  val contentBlurRadiusAsState
    @Composable
    get() =
      animateDpAsState(
        if (containsSearchResults) {
          BlurRadii.endInclusive
        } else {
          BlurRadii.start
        },
        label = "Content blur radius"
      )

  /**
   * Displays either the [content] or the [SearchTextField] that presents results for the query when
   * it is requested to be shown.
   *
   * @param query Content to be looked up.
   * @param onQueryChange Lambda invoked whenever the [query] changes.
   * @param resultsLoadable [Profile] results found by the [query].
   * @param modifier [Modifier] to be applied to the [SearchTextField].
   * @param content Content that can be replaced by the [SearchTextField].
   * @throws IllegalStateException If a [Replaceable] is already currently composed.
   */
  @Composable
  @Throws(IllegalStateException::class)
  fun Replaceable(
    query: String,
    onQueryChange: (query: String) -> Unit,
    resultsLoadable: ListLoadable<ProfileSearchResult>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
  ) {
    val coroutineScope = rememberCoroutineScope()
    val resultSearchTextFieldDialog = rememberResultSearchTextFieldPopup()

    ReplaceableCompositionReporterEffect()
    SearchResultsEffect(resultsLoadable)

    DisposableEffect(resultSearchTextFieldDialog) {
      resultSearchTextFieldDialog.doOnDidDismiss(::dismiss)
      onDispose {}
    }

    Filler {
      Accordion { willSearch ->
        if (willSearch) {
          resultSearchTextFieldDialog.Content(
            query,
            onQueryChange,
            resultsLoadable,
            modifier.resultSearchTextFieldHeightReporter(
              LocalDensity.current,
              coroutineScope,
              SearchTextFieldDefaults.spacing
            )
          )
        } else {
          ResultSearchTextFieldLayoutHeightResetEffect(coroutineScope)
          content()
        }
      }
    }
  }

  /** Shows the [SearchTextField]. */
  fun show() {
    isSearching = isReplaceableComposedState.value
  }

  /**
   * Displays either the [content] or the [ResultSearchTextField] that presents results for the
   * query when it is requested to be shown.
   *
   * This overload is stateless by default and is intended for previewing and testing purposes only.
   *
   * @param modifier [Modifier] to be applied to the [ResultSearchTextField].
   * @param query Content to be looked up.
   * @param onQueryChange Lambda invoked whenever the [query] changes.
   * @param resultsLoadable [Profile] results found by the [query].
   * @param content Content that can be replaced by the [ResultSearchTextField].
   * @throws IllegalStateException If a [Replaceable] is already currently composed.
   */
  @Composable
  @Throws(IllegalStateException::class)
  @VisibleForTesting
  internal fun Replaceable(
    modifier: Modifier = Modifier,
    query: String = "",
    onQueryChange: (query: String) -> Unit = {},
    resultsLoadable: ListLoadable<ProfileSearchResult> = ListLoadable.Loading(),
    content: @Composable () -> Unit = {}
  ) {
    Replaceable(query, onQueryChange, resultsLoadable, modifier, content)
  }

  /** Dismisses the [ResultSearchTextField]. */
  internal fun dismiss() {
    isSearching = false
  }

  /**
   * Effect that reports whether a [Replaceable] is composed, updating the value of
   * [isReplaceableComposedState].
   *
   * @throws IllegalStateException If a [Replaceable] is already currently composed.
   */
  @Composable
  @Throws(IllegalStateException::class)
  private fun ReplaceableCompositionReporterEffect() {
    DisposableEffect(isReplaceableComposedState) {
      check(!isReplaceableComposedState.value) { "A replaceable can only be composed once." }
      isReplaceableComposedState.value = true
      onDispose { isReplaceableComposedState.value = false }
    }
  }

  /**
   * Effect that reports whether results for the current search have been found and updates
   * [containsSearchResults] accordingly.
   *
   * @param resultsLoadable [Profile] results found by the query.
   */
  @Composable
  private fun SearchResultsEffect(resultsLoadable: ListLoadable<ProfileSearchResult>) {
    DisposableEffect(isSearching, resultsLoadable) {
      containsSearchResults = isSearching && resultsLoadable is ListLoadable.Populated
      onDispose { containsSearchResults = false }
    }
  }

  /**
   * Animates visibility changes of the [content], which are based on whether search is being
   * performed. Switches between one state to another with an accordion-like transition, collapsing
   * the [content] or expanding it for it to be displayed again.
   *
   * @param modifier [Modifier] applied to the underlying [AnimatedContent].
   * @param content Content to be collapsed and expanded.
   */
  @Composable
  private fun Accordion(
    modifier: Modifier = Modifier,
    content: @Composable (willSearch: Boolean) -> Unit
  ) {
    AnimatedContent(
      targetState = isSearching,
      modifier,
      transitionSpec = {
        slideInVertically(
          tween(
            AccordionBaselineAnimationDurationInMilliseconds,
            delayMillis = if (targetState) 16 else 0
          )
        ) {
          if (targetState) it / 2 else -it / 4
        } + fadeIn(tween(AccordionBaselineAnimationDurationInMilliseconds)) togetherWith
          slideOutVertically() + fadeOut(tween(durationMillis = 32)) using
          SizeTransform(clip = false) { _, targetSize ->
            keyframes {
              IntSize(targetSize.width, targetSize.height) at
                AccordionBaselineAnimationDurationInMilliseconds using
                FastOutLinearInEasing
            }
          }
      },
      label = "Accordion"
    ) {
      content(it)
    }
  }

  /**
   * Considers the [Composable] to which this [Modifier] is applied a [ResultSearchTextField] and
   * assigns its height to [resultSearchTextFieldHeight] at each change to it so that the overlaid
   * content can be properly padded/spaced.
   *
   * @param density [Density] with which the height in pixels of the layout is to be converted into
   *   [Dp].
   * @param coroutineScope [CoroutineScope] in which [Job]s that animate the
   *   [resultSearchTextFieldHeight] are launched.
   * @param spacing Amount of [Dp] that spaces a [SearchTextField] by default, obtainable through
   *   [SearchTextFieldDefaults.spacing].
   */
  private fun Modifier.resultSearchTextFieldHeightReporter(
    density: Density,
    coroutineScope: CoroutineScope,
    spacing: Dp
  ): Modifier {
    return if (resultSearchTextFieldHeight == 0.dp) {
      onSizeChanged {
        coroutineScope.launch {
          resultSearchTextFieldHeightAnimatable.animateTo(
            with(density) { it.height.toDp() } + spacing * 2
          )
        }
      }
    } else {
      this
    }
  }

  /**
   * Effect that zeroes the [resultSearchTextFieldHeight] once.
   *
   * @param coroutineScope [CoroutineScope] in which the [Job] that resets the
   *   [resultSearchTextFieldHeight] is launched.
   */
  @Composable
  private fun ResultSearchTextFieldLayoutHeightResetEffect(coroutineScope: CoroutineScope) {
    DisposableEffect(Unit) {
      coroutineScope.launch { resultSearchTextFieldHeightAnimatable.animateTo(0.dp) }
      onDispose {}
    }
  }

  /**
   * Fills the maximum space occupied by the [content] with the [fillerColor].
   *
   * @param modifier [Modifier] to be applied to the underlying [Box].
   * @param content Content whose size will be observed for the peak one to be defined as the
   *   underlying container's to which the [fillerColor] will be applied.
   */
  @Composable
  private fun Filler(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(modifier.fillerPeakSizeReporter()) {
      AnimatedVisibility(
        visible = isSearching,
        Modifier.testTag(FillerTag),
        EnterTransition.None,
        exit =
          fadeOut(
            tween(
              durationMillis = AccordionBaselineAnimationDurationInMilliseconds,
              delayMillis = AccordionBaselineAnimationDurationInMilliseconds
            )
          ),
        label = "Filler"
      ) {
        Canvas(Modifier) { drawRect(fillerColor, Offset.Zero, fillerPeakSize) }
      }

      content()
    }
  }

  /**
   * Observes changes in size and updates [SearchableScope.fillerPeakSizeReporter] when a dimension
   * is greater than the previous composed one.
   */
  private fun Modifier.fillerPeakSizeReporter(): Modifier {
    return onSizeChanged {
      if (!isSearching) {
        val changedSize = it.toSize()
        if (fillerPeakSize.isUnspecified) {
          fillerPeakSize = changedSize
        } else {
          if (fillerPeakSize.width.isNaN() || fillerPeakSize.width < changedSize.width) {
            fillerPeakSize = fillerPeakSize.copy(width = changedSize.width)
          }
          if (fillerPeakSize.height.isNaN() || fillerPeakSize.height < changedSize.height) {
            fillerPeakSize = fillerPeakSize.copy(height = changedSize.height)
          }
        }
      }
    }
  }

  companion object {
    /**
     * Duration of an [Accordion]'s animation in milliseconds. Note that the delays aren't accounted
     * for.
     */
    @Suppress("ConstPropertyName")
    private const val AccordionBaselineAnimationDurationInMilliseconds = 128

    /** Tag that identifies a [Searchable]'s [Filler] for testing purposes. */
    @Suppress("ConstPropertyName") internal const val FillerTag = "searchable-filler"

    /** Radii of the blur that should be applied to the content. */
    internal val BlurRadii = 0.dp..16.dp
  }
}
