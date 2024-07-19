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

package br.com.orcinus.orca.composite.timeline.search.content

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import br.com.orcinus.orca.composite.timeline.InternalTimelineApi
import br.com.orcinus.orca.composite.timeline.search.Searchable
import br.com.orcinus.orca.composite.timeline.search.field.ResultSearchTextField
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearchResult
import br.com.orcinus.orca.platform.autos.kit.input.text.SearchTextField
import br.com.orcinus.orca.platform.autos.kit.input.text.SearchTextFieldDefaults
import br.com.orcinus.orca.platform.focus.rememberImmediateFocusRequester
import com.jeanbarrossilva.loadable.list.ListLoadable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Scope of a [Searchable] in which by which the main content (the one to be shown behind the
 * [SearchTextField]) is set.
 *
 * @property replacementScope [SearchableReplacementScope]
 * @property isReplaceableComposedState [MutableState] whose [Boolean] determines whether the
 *   content to be replaced by the [ResultSearchTextField] is currently composed.
 * @see content
 */
class SearchableMainContentScope
internal constructor(
  private val replacementScope: SearchableReplacementScope,
  private val isReplaceableComposedState: MutableState<Boolean>
) {
  /** [Animatable] by which the height of the [ResultSearchTextField] is held and animated. */
  private val searchTextFieldLayoutHeightAnimatable =
    Animatable(initialValue = 0.dp, Dp.VectorConverter)

  /** Content to be shown by default and that can be replaced. */
  internal var content by mutableStateOf<(@Composable () -> Unit)?>(null)
    private set

  /** Height of the [SearchTextField], or zeroed in case search isn't being performed. */
  val searchTextFieldLayoutHeight by searchTextFieldLayoutHeightAnimatable.asState()

  /**
   * [State] with the radius of the blur that should be applied to the [content].
   *
   * @see BlurRadii
   */
  val contentBlurRadiusAsState
    @Composable
    get() =
      animateDpAsState(
        if (isReplaceableComposedState.value && replacementScope.isSearching) {
          BlurRadii.endInclusive
        } else {
          BlurRadii.start
        },
        label = "Content blur radius"
      )

  /**
   * Sets the content to be replaced by the [ResultSearchTextField].
   *
   * @param content Content to be shown by default and that can be replaced.
   */
  fun content(content: @Composable () -> Unit) {
    this.content = content
  }

  /**
   * Displays either the [content] or the [SearchTextField] that presents results for the query when
   * it is requested to be shown.
   *
   * @param query Content to be looked up.
   * @param onQueryChange Lambda invoked whenever the [query] changes.
   * @param profileSearchResultsLoadable [Profile] results found by the [query].
   * @param modifier [Modifier] to be applied to the [SearchTextField].
   * @param content Content that can be replaced by the [SearchTextField].
   */
  @Composable
  fun Replaceable(
    query: String,
    onQueryChange: (query: String) -> Unit,
    profileSearchResultsLoadable: ListLoadable<ProfileSearchResult>,
    modifier: Modifier = Modifier,
    content: @Composable SearchableReplacementScope.() -> Unit
  ) {
    ReplaceableCompositionReporterEffect()

    Accordion { willSearch ->
      val coroutineScope = rememberCoroutineScope()

      if (willSearch) {
        SearchTextFieldPopup(
          coroutineScope,
          query,
          onQueryChange,
          profileSearchResultsLoadable,
          modifier
        )
      } else {
        SearchTextFieldLayoutHeightResetEffect(coroutineScope)
        replacementScope.content()
      }
    }
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
   * @param profileSearchResultsLoadable [Profile] results found by the [query].
   * @param content Content that can be replaced by the [ResultSearchTextField].
   */
  @Composable
  @InternalTimelineApi
  @VisibleForTesting
  internal fun Replaceable(
    modifier: Modifier = Modifier,
    query: String = "",
    onQueryChange: (query: String) -> Unit = {},
    profileSearchResultsLoadable: ListLoadable<ProfileSearchResult> = ListLoadable.Loading(),
    content: @Composable SearchableReplacementScope.() -> Unit = {}
  ) {
    Replaceable(query, onQueryChange, profileSearchResultsLoadable, modifier, content)
  }

  /**
   * Effect that reports whether a [Replaceable] is composed, updating the value of
   * [isReplaceableComposedState].
   */
  @Composable
  private fun ReplaceableCompositionReporterEffect() {
    DisposableEffect(isReplaceableComposedState) {
      isReplaceableComposedState.value = true
      onDispose { isReplaceableComposedState.value = false }
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
      targetState = replacementScope.isSearching,
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
   * Considers the [Composable] on which this [Modifier] has been applied to be the layout of the
   * [ResultSearchTextField]; ultimately, that implies in it having its height changes set as the
   * value of [searchTextFieldLayoutHeight] so that the main content can be properly padded/spaced.
   *
   * @param density [Density] with which the height in pixels of the layout is to be converted into
   *   [Dp].
   * @param coroutineScope [CoroutineScope] in which [Job]s that animate the
   *   [searchTextFieldLayoutHeight] are launched.
   * @param spacing Amount of [Dp] that spaces a [SearchTextField] by default, obtainable through
   *   [SearchTextFieldDefaults.spacing].
   */
  private fun Modifier.searchTextFieldLayout(
    density: Density,
    coroutineScope: CoroutineScope,
    spacing: Dp
  ): Modifier {
    return if (searchTextFieldLayoutHeight == 0.dp) {
      onSizeChanged {
        coroutineScope.launch {
          searchTextFieldLayoutHeightAnimatable.animateTo(
            with(density) { it.height.toDp() } + spacing * 2
          )
        }
      }
    } else {
      this
    }
  }

  /**
   * [Popup] by which a [ResultSearchTextField] is displayed, whose changes in height are observed
   * and then propagated to [searchTextFieldLayoutHeight]. It is pre-offset and -padded based on the
   * default spacing of a [SearchTextField].
   *
   * @param coroutineScope [CoroutineScope] in which [Job]s that animate the
   *   [searchTextFieldLayoutHeight] are launched.
   * @param query Content to be looked up.
   * @param onQueryChange Lambda invoked whenever the [query] changes.
   * @param profileSearchResultsLoadable [Profile] results found by the [query].
   * @param modifier [Modifier] to be applied to the [ResultSearchTextField].
   * @see SearchTextFieldDefaults.spacing
   */
  @Composable
  private fun SearchTextFieldPopup(
    coroutineScope: CoroutineScope,
    query: String,
    onQueryChange: (query: String) -> Unit,
    profileSearchResultsLoadable: ListLoadable<ProfileSearchResult>,
    modifier: Modifier = Modifier
  ) {
    val density = LocalDensity.current
    val searchTextFieldSpacing = SearchTextFieldDefaults.spacing
    val searchTextFieldSpacingInPixels =
      remember(density, searchTextFieldSpacing) {
        with(density) { searchTextFieldSpacing.roundToPx() }
      }

    Popup(
      offset = IntOffset(x = 0, y = searchTextFieldSpacingInPixels),
      properties = PopupProperties(focusable = true)
    ) {
      Box(Modifier.padding(horizontal = searchTextFieldSpacing)) {
        ResultSearchTextField(
          query,
          onQueryChange,
          onDismissal = replacementScope::dismiss,
          profileSearchResultsLoadable,
          modifier
            .focusRequester(rememberImmediateFocusRequester())
            .searchTextFieldLayout(density, coroutineScope, searchTextFieldSpacing)
            .fillMaxWidth()
        )
      }
    }
  }

  /**
   * Effect that zeroes the [searchTextFieldLayoutHeight] once.
   *
   * @param coroutineScope [CoroutineScope] in which the [Job] that resets the
   *   [searchTextFieldLayoutHeight] is launched.
   */
  @Composable
  private fun SearchTextFieldLayoutHeightResetEffect(coroutineScope: CoroutineScope) {
    DisposableEffect(Unit) {
      coroutineScope.launch { searchTextFieldLayoutHeightAnimatable.animateTo(0.dp) }
      onDispose {}
    }
  }

  companion object {
    /**
     * Duration of an [Accordion]'s animation in milliseconds. Note that the delays aren't accounted
     * for.
     */
    @Suppress("ConstPropertyName")
    private const val AccordionBaselineAnimationDurationInMilliseconds = 128

    /** Radii of the blur that should be applied to the [content]. */
    internal val BlurRadii = 0.dp..16.dp
  }
}
