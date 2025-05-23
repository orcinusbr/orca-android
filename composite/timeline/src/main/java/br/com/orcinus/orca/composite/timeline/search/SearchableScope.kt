/*
 * Copyright © 2024–2025 Orcinus
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
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.composite.timeline.search.field.SearchTextFieldPopup
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearchResult
import br.com.orcinus.orca.platform.autos.kit.input.text.SearchTextField
import com.jeanbarrossilva.loadable.list.ListLoadable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Scope of a [Searchable] in which the [SearchTextField] can be either shown or dismissed.
 *
 * @param searchTextFieldOffset Amount in both axes by which the [SearchTextField] is offset.
 * @param searchTextFieldPadding Space to surround the [SearchTextField] with.
 * @property isReplaceableComposedState [MutableState] whose [Boolean] determines whether the
 *   content to be replaced by the [SearchTextField] is currently composed.
 */
class SearchableScope
internal constructor(
  private val searchTextFieldOffset: DpOffset,
  private val searchTextFieldPadding: PaddingValues,
  private val isReplaceableComposedState: MutableState<Boolean>
) {
  /** [Animatable] by which the height of the [SearchTextField] is held and animated. */
  private val searchTextFieldHeightAnimatable = Animatable(initialValue = 0.dp, Dp.VectorConverter)

  /** Whether the [SearchTextField] is currently being shown. */
  var isSearching by mutableStateOf(false)
    private set

  /** Whether the search currently being performed has yielded any results. */
  var containsSearchResults by mutableStateOf(false)
    private set

  /** Height of the [SearchTextField], or zeroed in case search isn't being performed. */
  val searchTextFieldHeight by searchTextFieldHeightAnimatable.asState()

  /** Alias for [searchTextFieldHeight]. */
  @Deprecated(
    "Renamed to \"searchTextFieldHeight\".",
    ReplaceWith(
      "searchTextFieldHeight",
      imports = ["br.com.orcinus.orca.composite.timeline.search.SearchableScope"]
    )
  )
  inline val searchTextFieldLayoutHeight
    get() = searchTextFieldHeight

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
   * Creates a [FiniteAnimationSpec] with which the content to be replaced is animated in and out
   * based on whether search is currently being performed — that is, the [SearchTextField] is
   * displayed. Should also be applied to any other animation derived from such transition.
   *
   * @param T Value to be animated.
   * @see Replaceable
   */
  fun <T> replacementAnimationSpec() = internalReplacementAnimationSpec<T>()

  /**
   * Displays either the [content] or the [SearchTextField] that presents results for the query when
   * it is requested to be shown. Such replacement by one composable or the other transitions with
   * an animation specified by the [internalReplacementAnimationSpec], which should be applied to
   * all animations derived from this change.
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

    ReplaceableCompositionReporterEffect()
    SearchResultsEffect(resultsLoadable)

    Accordion { willSearch ->
      if (willSearch) {
        SearchTextFieldPopup(
          query,
          onQueryChange,
          resultsLoadable,
          onDidDismiss = ::dismiss,
          modifier.resultSearchTextFieldHeightReporter(coroutineScope).fillMaxWidth(),
          Alignment.TopCenter,
          searchTextFieldOffset,
          searchTextFieldPadding
        )
      } else {
        ResultSearchTextFieldLayoutHeightResetEffect(coroutineScope)
        content()
      }
    }
  }

  /** Shows the [SearchTextField]. */
  fun show() {
    isSearching = isReplaceableComposedState.value
  }

  /**
   * Displays either the [content] or the [SearchTextField] that presents results for the query when
   * it is requested to be shown. Such replacement by one composable or the other transitions with
   * an animation specified by the [internalReplacementAnimationSpec], which should be applied to
   * all animations derived from this change.
   *
   * This overload is stateless by default and is intended for previewing and testing purposes only.
   *
   * @param modifier [Modifier] to be applied to the [SearchTextField].
   * @param query Content to be looked up.
   * @param onQueryChange Lambda invoked whenever the [query] changes.
   * @param resultsLoadable [Profile] results found by the [query].
   * @param content Content that can be replaced by the [SearchTextField].
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

  /** Dismisses the [SearchTextField]. */
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
          internalReplacementAnimationSpec(
            delayInMilliseconds =
              if (targetState) {
                16
              } else {
                0
              }
          )
        ) {
          if (targetState) it / 2 else -it / 4
        } + fadeIn(replacementAnimationSpec()) togetherWith
          slideOutVertically(replacementAnimationSpec()) +
            fadeOut(
              internalReplacementAnimationSpec(
                durationInMilliseconds = AccordionBaselineAnimationDurationInMilliseconds / 2
              )
            ) using
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
   * Creates a [FiniteAnimationSpec] with which the content to be replaced by a [SearchTextField] is
   * animated in and out. Should also be applied to any other animation derived from such
   * transition; to do so, call the public variant of this method without parameters.
   *
   * @param T Value to be animated.
   * @param durationInMilliseconds Duration of the animation in milliseconds.
   * @param delayInMilliseconds Time in milliseconds to be awaited before the animation starts.
   * @see Replaceable
   * @see replacementAnimationSpec
   */
  private fun <T> internalReplacementAnimationSpec(
    durationInMilliseconds: Int = AccordionBaselineAnimationDurationInMilliseconds,
    delayInMilliseconds: Int = 0
  ): FiniteAnimationSpec<T> = tween<T>(durationInMilliseconds, delayInMilliseconds)

  /**
   * Considers the [Composable] to which this [Modifier] is applied a [SearchTextField] and assigns
   * its height to [searchTextFieldHeight] at each change to it so that the overlaid content can be
   * properly padded/spaced.
   *
   * @param coroutineScope [CoroutineScope] in which [Job]s that animate the [searchTextFieldHeight]
   *   are launched.
   */
  private fun Modifier.resultSearchTextFieldHeightReporter(
    coroutineScope: CoroutineScope
  ): Modifier {
    return if (searchTextFieldHeight == 0.dp) {
      layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        val height = placeable.height
        coroutineScope.launch { searchTextFieldHeightAnimatable.animateTo(height.toDp()) }
        layout(placeable.width, height) { placeable.place(x = 0, y = 0) }
      }
    } else {
      this
    }
  }

  /**
   * Effect that zeroes the [searchTextFieldHeight] once.
   *
   * @param coroutineScope [CoroutineScope] in which the [Job] that resets the
   *   [searchTextFieldHeight] is launched.
   */
  @Composable
  private fun ResultSearchTextFieldLayoutHeightResetEffect(coroutineScope: CoroutineScope) {
    DisposableEffect(Unit) {
      coroutineScope.launch { searchTextFieldHeightAnimatable.animateTo(0.dp) }
      onDispose {}
    }
  }

  companion object {
    /**
     * Duration of an [Accordion]'s animation in milliseconds. Note that the delays aren't accounted
     * for.
     */
    @Suppress("ConstPropertyName")
    private const val AccordionBaselineAnimationDurationInMilliseconds = 64

    /** Radii of the blur that should be applied to the content. */
    internal val BlurRadii = 0.dp..16.dp
  }
}
