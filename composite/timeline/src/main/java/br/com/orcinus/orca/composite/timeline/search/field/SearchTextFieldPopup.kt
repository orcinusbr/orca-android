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

package br.com.orcinus.orca.composite.timeline.search.field

import android.app.Dialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.util.Size
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.Window
import android.view.WindowManager
import androidx.annotation.DeprecatedSinceApi
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.RequiresApi
import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.window.Popup
import androidx.core.graphics.ColorUtils
import br.com.orcinus.orca.composite.timeline.InternalTimelineApi
import br.com.orcinus.orca.composite.timeline.R
import br.com.orcinus.orca.composite.timeline.avatar.SmallAvatar
import br.com.orcinus.orca.composite.timeline.search.field.interop.asGravity
import br.com.orcinus.orca.composite.timeline.search.field.interop.layoutDirectionOf
import br.com.orcinus.orca.composite.timeline.search.field.interop.setPadding
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearchResult
import br.com.orcinus.orca.core.sample.feed.profile.account.sample
import br.com.orcinus.orca.ext.coroutines.getValue
import br.com.orcinus.orca.ext.coroutines.setValue
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.kit.action.button.icon.HoverableIconButton
import br.com.orcinus.orca.platform.autos.kit.bottom
import br.com.orcinus.orca.platform.autos.kit.input.text.SearchTextField
import br.com.orcinus.orca.platform.autos.kit.input.text.SearchTextFieldDefaults
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview
import br.com.orcinus.orca.platform.core.sample
import br.com.orcinus.orca.platform.focus.rememberImmediateFocusRequester
import br.com.orcinus.orca.std.image.compose.SomeComposableImageLoader
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.loadable.list.serializableListOf
import com.jeanbarrossilva.loadable.list.toListLoadable
import com.jeanbarrossilva.loadable.placeholder.MediumTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.SmallTextualPlaceholder
import java.lang.ref.WeakReference
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/** Default padding applied to a [SearchTextFieldPopup]: none. */
private val Unpadded = PaddingValues()

/** Duration in milliseconds of the appearance animation for results. */
private const val ResultAppearanceAnimationDurationInMilliseconds = 56

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
 * since it was the solution adopted by Orca prior to 0.5.0 and had issues such as inability to have
 * both focusability and dismissibility via an outside click, and negative
 * text-editing-decorators-Y-offsetting (see
 * [#378](https://github.com/orcinusbr/orca-android/pull/378)).
 *
 * @property contextRef [WeakReference] to the [Context] in which this popup is to be displayed.
 * @property hostViewTreeOwner Owner of the created [HostView].
 * @property parentCompositionContext Provider of the [CompositionLocal]s to be "inherited".
 * @property density [Density] for converting [Dp]s into absolute pixels when changing the offset.
 * @property searchTextFieldDefaultShape [Shape] that clips a [SearchTextField] by default.
 */
@Stable
private class SearchTextFieldPopup(
  private val contextRef: WeakReference<Context>,
  private val hostViewTreeOwner: ViewTreeOwner?,
  private val parentCompositionContext: CompositionContext,
  private val density: Density,
  private val searchTextFieldDefaultShape: Shape
) {
  /**
   * [CoroutineScope] in which changes to the size of a [HostView] and the padding are observed,
   * instantiated when this popup enters the composition and both cancelled and recreated when it
   * exits it (which is whenever it gets dismissed).
   *
   * @see hostViewSizeFlow
   * @see paddingFlow
   * @see createCoroutineScope
   * @see Dialog.dismiss
   * @see dismiss
   */
  private var coroutineScope = createCoroutineScope()

  /**
   * [MutableStateFlow] containing the dimensions of the attached [HostView] in absolute pixels. Its
   * value is initially `null`, instantiated and set each time this popup is shown, mutated upon
   * measurement and nullified whenever it is detached (which is when this popup gets dismissed).
   *
   * @see createHostView
   * @see MutableStateFlow.value
   * @see show
   * @see Dialog.dismiss
   * @see dismiss
   */
  private val hostViewSizeFlow = MutableStateFlow<MutableSize?>(null)

  /**
   * Alias for `hostViewSizeFlow.value`.
   *
   * @see hostViewSizeFlow
   */
  private var hostViewSize by hostViewSizeFlow

  /**
   * Amount in both bi-dimensional axes by which this popup is offset in absolute pixels; (0, 0) by
   * default. Its observability differs completely from [paddingFlow]'s and partially from
   * [hostViewSizeFlow]'s in that its value is mutable _and_ is set to the same instance with the
   * respective modifications when an offset is applied.
   *
   * @see setOffset
   */
  private var offset by mutableStateOf(MutableOffset(x = 0, y = 0))

  /**
   * [MutableStateFlow] containing the amount of space in absolute pixels added to the edges of a
   * [HostView] (which is zero by default). Its value is immutable — differing from the
   * [hostViewSize] and the [offset] — because each reset should be observable for it to be applied.
   *
   * @see setPadding
   * @see createHostView
   */
  private val paddingFlow = MutableStateFlow(Unpadded)

  /**
   * Listener that gets notified after the [delegate] is dismissed.
   *
   * @see Dialog.dismiss
   * @see dismiss
   * @see setOnDidDismissListener
   */
  private var onDidDismissListener: (() -> Unit)? = null

  /**
   * [Dialog] by which a [HostView] is displayed; `null` if the [context] has been garbage-collected
   * by the time it is instantiated. Resets the [coroutineScope] and notifies the
   * [onDidDismissListener] whenever it gets dismissed.
   *
   * @see createHostView
   * @see Dialog.dismiss
   */
  private val delegate by lazy {
    context?.let { context ->
      Dialog(context, br.com.orcinus.orca.platform.autos.R.style.Theme_Autos).apply {
        /*
         * ResultSearchTextField's elevation is clipped by default because the decor view's
         * descendants are outlined based on their bounds, clip their children and delimit them to
         * their parent's padding — all of which prevents the cast shadow from being completely
         * visible within the dialog.
         *
         * Orca's workaround differs slightly from that adopted by Compose Popup's PopupLayout (as
         * of Compose 1.5.11): rather than allocating space for the shadow by instantiating a
         * provider of an invisible outline, we disable the aforementioned clipping in all those
         * views and define their background as an inset, transparent one, setting it as their
         * outline provider.
         */
        val elevationInPx = with(density) { SearchTextFieldDefaults.Elevation.roundToPx() }
        val elevationInsetBackground = InsetDrawable(transparentBackground, elevationInPx)
        window?.decorView?.traverse { descendant ->
          if (descendant is ViewGroup) {
            descendant.clipToPadding = false
            descendant.clipChildren = false
            descendant.background = elevationInsetBackground
            descendant.outlineProvider = ViewOutlineProvider.BACKGROUND
          }
        }

        /*
         * Dialog.setCanceledOnTouchOutside(Boolean) requires the dialog's window to wrap its
         * content, since it initially matches its parent's — decor view's — size by default and,
         * thus, no interactions are considered external to it.
         */
        window?.setLayout(
          WindowManager.LayoutParams.WRAP_CONTENT,
          WindowManager.LayoutParams.WRAP_CONTENT
        )

        window?.setBackgroundDrawable(transparentBackground)
        setOnDismissListener {
          coroutineScope.cancel("Popup was dismissed.")
          coroutineScope = createCoroutineScope()
          onDidDismissListener?.invoke()
        }
        setCanceledOnTouchOutside(true)
      }
    }
  }

  /** [Modifier] applied to the [ResultSearchTextField]. */
  private var modifier by mutableStateOf<Modifier>(Modifier)

  /** Content being looked up in the [ResultSearchTextField]. */
  private var query by mutableStateOf("")

  /** Lambda invoked whenever the query changes in the [ResultSearchTextField]. */
  private var onQueryChange by mutableStateOf(NoOpOnQueryChange)

  /** [Profile] results found for the [query] in the [ResultSearchTextField]. */
  private var resultsLoadable by mutableStateOf(EmptyResultsLoadable)

  /** [Context] referenced by the [contextRef]. */
  private inline val context
    get() = contextRef.get()

  /**
   * Horizontal and vertical dimensions in both bi-dimensional axes. Differs from Android's [Size]
   * and Compose's [IntSize] in that its values are mutable, removing the need for allocation of a
   * new instance each time they change.
   *
   * @see asIntSize
   */
  private class MutableSize private constructor() {
    /**
     * [IntSize] returned when this [MutableSize] is converted into such a class. `null` when a
     * conversion has never occurred before or either the [width] or the [height] have been changed
     * since it was last converted.
     *
     * @see asIntSize
     */
    private var intSize: IntSize? = null

    constructor(@IntRange(from = 0) width: Int, @IntRange(from = 0) height: Int) : this() {
      this.width = width
      this.height = height
    }

    /** Amount of absolute pixels horizontally. */
    @IntRange(from = 0)
    var width = 0
      set(width) {
        if (field != width) {
          field = width
          intSize = null
        }
      }

    /** Amount of absolute pixels vertically. */
    @IntRange(from = 0)
    var height = 0
      set(height) {
        if (field != height) {
          field = height
          intSize = null
        }
      }

    /**
     * Converts this into a Compose [IntSize].
     *
     * Note that an instance of that class is not necessarily created each time this method gets
     * called; rather, it is instantiated only when either this [MutableSize]'s [width] or [height]
     * have changed since the last conversion, or it has never been converted before.
     */
    fun asIntSize() = intSize ?: IntSize(width, height).also { intSize = it }
  }

  /**
   * Addition to a rendering position in both bi-dimensional axes. Differs from Compose's
   * [IntOffset] in that its values are mutable, removing the need for allocation of a new instance
   * each time they change.
   *
   * @property x Absolute pixels added to the X-axis.
   * @property y Absolute pixels added to the Y-axis.
   */
  private class MutableOffset(var x: Int, var y: Int) {
    override fun toString() = "($x, $y)"
  }

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
    Dimmer()
  }

  /**
   * Changes the position of this popup relative to the window in which it is displayed.
   *
   * @param alignment [Alignment] from which the positioning is to be performed.
   * @throws UnsupportedOperationException If the [alignment] is neither predefined nor
   *   bi-dimensional.
   */
  @Throws(UnsupportedOperationException::class)
  fun setAlignment(alignment: Alignment) {
    coroutineScope.launch {
      window { attributes ->
        layoutDirectionOf(decorView)?.let { layoutDirection ->
          val hostViewSize = hostViewSizeFlow.filterNotNull().first()
          val space = IntSize(decorView.width, decorView.height)
          attributes.gravity = alignment.asGravity(hostViewSize.asIntSize(), space, layoutDirection)
        } != null
      }
    }
  }

  /**
   * Changes the offset of this popup, which is (0, 0) by default. Ultimately, repositions it by
   * removing the previously defined offset and adding the specified one to both bi-dimensional axes
   * of the window.
   *
   * @param offset Amounts in [Dp] to offset by.
   */
  fun setOffset(offset: DpOffset) = window {
    val previousOffset = this@SearchTextFieldPopup.offset
    val nextOffsetX = with(density) { offset.x.roundToPx() }
    val nextOffsetY = with(density) { offset.y.roundToPx() }
    var didChangeAttributes = false
    if (previousOffset.x != nextOffsetX) {
      it.x += nextOffsetX - previousOffset.x
      didChangeAttributes = true
      previousOffset.x = nextOffsetX
      this@SearchTextFieldPopup.offset = previousOffset
    }
    if (previousOffset.y != nextOffsetY) {
      it.y += nextOffsetY - previousOffset.y
      didChangeAttributes = true
      previousOffset.y = nextOffsetY
      this@SearchTextFieldPopup.offset = previousOffset
    }
    didChangeAttributes
  }

  /**
   * Changes the amount of space surrounding this popup (none by default). Differs from an offset in
   * that it influences the overall size upon measurement. Under the hood, sets the value of the
   * [MutableStateFlow], which is collected upon display until shown, and applied to the created
   * [HostView].
   *
   * @param padding Space to be added to the edges of this popup.
   * @see setOffset
   * @see MutableStateFlow.value
   */
  fun setPadding(padding: PaddingValues) {
    paddingFlow.value = padding
  }

  /**
   * Schedules the execution of an action for after the popup has been dismissed. Ultimately, allows
   * the standalone composable to replace the current listener by another one upon a recomposition
   * due to the previously set `onDidDismiss` lambda having changed, and remove it when it leaves
   * composition.
   *
   * @param onDidDismissListener Listener to be notified of dismissals, or `null` for removing the
   *   previously defined one.
   */
  fun setOnDidDismissListener(onDidDismissListener: (() -> Unit)?) {
    this.onDidDismissListener = onDidDismissListener
  }

  /** Creates a [coroutineScope] for this popup. */
  private fun createCoroutineScope() = CoroutineScope(Dispatchers.Main.immediate)

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
          check(!it.isShowing) { "Cannot perform simultaneous compositions of a popup!" }
        }
      }
    }

  /**
   * Effect that triggers recompositions on a [HostView] by assigning the given parameters to this
   * popup's [State]- and [MutableStateFlow]-based properties. Each of them is then reset upon a
   * decomposition, with a default, empty value.
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

  /**
   * Effect that dims the background content either by changing the window's dim amount on API level
   * equal to/greater than 35 (VanillaIceCream) or laying out a fullscreen dark shade on top of it
   * on older OS versions. For the reasoning on such distinction and its implications, see
   * [DimmingEffect].
   *
   * Whether the dimming occurs will depend on the found results: if there is at least one, then it
   * does so; otherwise, it does not.
   *
   * @param modifier [Modifier] to (possibly) be applied to the underlying [Scrim].
   * @see resultsLoadable
   */
  @Composable
  private fun Dimmer(modifier: Modifier = Modifier) {
    val shouldDim by remember { derivedStateOf { resultsLoadable is ListLoadable.Populated } }
    val dimming by animateFloatAsState(if (shouldDim) .05f else 0f, label = "Dimming")

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
      DimmingEffect(dimming)
    } else {
      Scrim(alpha = dimming, modifier)
    }
  }

  /**
   * API level 35 (VanillaIceCream) deprecated and made no-ops requests for setting the color of the
   * status bars, mean through which the popup makes itself look like a fullscreen one (changing the
   * alpha to match [Scrim]'s) in older OS versions; such change is worked-around by adding the
   * dim-behind flag. By doing so, however, their appearance is undocumentedly enforced to light
   * _if_ the window is offset.
   *
   * This effect dims the content beneath this popup.
   *
   * @param dimming Background content dimming opacity, with `0f` = transparent; and `1f` = opaque.
   * @see WindowManager.LayoutParams.FLAG_DIM_BEHIND
   * @see setOffset
   */
  @Composable
  @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
  private fun DimmingEffect(@FloatRange(from = .0, to = 1.0) dimming: Float) {
    val window = delegate?.window ?: return

    DisposableEffect(window, dimming) {
      if (dimming > 0f) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
      }
      window.setDimAmount(dimming)
      onDispose { window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND) }
    }
  }

  /**
   * Dark shade for contrasting the [ResultSearchTextField] with the content laid out behind it.
   *
   * @param alpha Opacity, with `0f` = transparent; and `1f` = opaque.
   * @param modifier [Modifier] to be applied to the underlying [Canvas].
   */
  @Composable
  @DeprecatedSinceApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
  @Suppress("DEPRECATION")
  private fun Scrim(@FloatRange(from = .0, to = 1.0) alpha: Float, modifier: Modifier = Modifier) {
    val window = delegate?.window ?: return
    val statusBarsColorInArgb = remember(window, window::getStatusBarColor)

    DisposableEffect(window, statusBarsColorInArgb, alpha) {
      window.statusBarColor =
        ColorUtils.setAlphaComponent(statusBarsColorInArgb, lerp(0, 255, alpha))
      onDispose { window.statusBarColor = statusBarsColorInArgb }
    }

    Canvas(
      modifier.layout { measurable, constraints ->
        layout(width = 0, height = 0) {
          measurable
            .measure(Constraints.fixed(constraints.maxWidth, constraints.maxHeight))
            .place(x = 0, y = 0)
        }
      }
    ) {
      drawRect(Color.Black.copy(alpha = alpha))
    }
  }

  /** Shows this popup, by which the [ResultSearchTextField] is displayed. */
  private fun show() {
    val delegate = delegate ?: return
    val hostViewTreeOwner = hostViewTreeOwner ?: return
    val hostView = createHostView() ?: return
    hostViewTreeOwner.own(hostView)
    delegate.setContentView(hostView)
    delegate.show()
  }

  /**
   * Creates an [AbstractComposeView] by which a [ResultSearchTextField] is hosted, whose tree
   * ownership is configured upon a request to show this popup and deconfigured whenever it is
   * dismissed. Each measurement is observed and propagated to [hostViewSize], and every set padding
   * is applied.
   *
   * @return The [HostView], or `null` in case this method gets called after the [context] has been
   *   garbage-collected.
   * @see hostViewTreeOwner
   * @see show
   * @see Dialog.dismiss
   * @see dismiss
   * @see setPadding
   */
  private fun createHostView(): HostView? =
    context?.let { context ->
      object : HostView(context, parentCompositionContext) {
        init {
          coroutineScope.launch { paddingFlow.collect(::setPadding) }
        }

        @Composable
        override fun Content() =
          ResultSearchTextField(
            query,
            onQueryChange,
            onDismissal = ::dismiss,
            resultsLoadable,
            Modifier.focusRequester(rememberImmediateFocusRequester()) then modifier
          )

        override fun onLaidOut(isChanged: Boolean) {
          if (isChanged) {
            val previousHostViewSize = hostViewSize
            if (previousHostViewSize == null) {
              hostViewSize = MutableSize(width, height)
            } else {
              previousHostViewSize.width = width
              previousHostViewSize.height = height
            }
          }
        }

        override fun onDetachedFromWindow() {
          super.onDetachedFromWindow()
          hostViewSize = null
        }
      }
    }

  /** Dismisses this popup, by which the [ResultSearchTextField] is displayed. */
  private fun dismiss() {
    delegate?.dismiss()
  }

  /**
   * Resets this popup's window attributes.
   *
   * @param attribute Lambda that receives both the [delegate]'s [Window] and its attributes,
   *   returning whether changes have been performed to them — which, in turn, determines if they
   *   will be reset. Is not invoked in case the [context] has been garbage-collected.
   * @see Window.getAttributes
   */
  @OptIn(ExperimentalContracts::class)
  private inline fun window(attribute: Window.(attributes: WindowManager.LayoutParams) -> Boolean) {
    contract { callsInPlace(attribute, InvocationKind.EXACTLY_ONCE) }
    val window = delegate?.window
    val attributes = window?.attributes
    if (window != null && attributes != null && window.attribute(attributes)) {
      window.attributes = attributes
    }
  }

  private companion object {
    /**
     * [delegate]'s [Window]'s and its decor [View]'s descendants' background.
     *
     * @see Window.getDecorView
     * @see View.getBackground
     */
    @JvmStatic private val transparentBackground = ColorDrawable(AndroidColor.TRANSPARENT)
  }
}

/**
 * Rather than a text field (as the name suggests), this function is an alias for the
 * [SearchTextFieldPopup] composable which merely exists to signalize the deprecation of the public
 * [SearchTextField] with results — such composable is to be composed only by that popup and is not
 * available for external referencing.
 *
 * @param modifier [Modifier] to be applied to the [SearchTextField].
 * @param query Content to be looked up.
 * @param onQueryChange Lambda invoked whenever the [query] changes.
 * @param resultsLoadable [Profile] results found for [query].
 * @param onDidDismiss Operation performed after this popup is dismissed.
 * @param alignment Positioning relative to the window.
 * @param offset Amount in both axes by which this popup is offset.
 * @param padding Space to surround the [SearchTextField] with.
 * @throws UnsupportedOperationException If the [alignment] is neither predefined nor
 *   bi-dimensional.
 */
@Composable
@Deprecated(
  "ResultSearchTextField is a part of the internals of a SearchTextFieldPopup as of Orca 0.5.0. " +
    "External consumers do not have access to it; thus, calling this composable displays such a " +
    "popup instead of a SearchTextField containing results for a given query.",
  ReplaceWith(
    "SearchTextFieldPopup(modifier, query, onQueryChange, resultsLoadable, onDidDismiss, " +
      "alignment, offset, padding)"
  )
)
@InternalTimelineApi
@Throws(UnsupportedOperationException::class)
@VisibleForTesting
fun ResultSearchTextField(
  modifier: Modifier = Modifier,
  query: String = "",
  onQueryChange: (query: String) -> Unit = NoOpOnQueryChange,
  resultsLoadable: ListLoadable<ProfileSearchResult> = EmptyResultsLoadable,
  onDidDismiss: () -> Unit = NoOpOnDismissal,
  alignment: Alignment = Alignment.TopStart,
  offset: DpOffset = DpOffset.Zero,
  padding: PaddingValues = Unpadded
) =
  SearchTextFieldPopup(
    modifier,
    query,
    onQueryChange,
    resultsLoadable,
    onDidDismiss,
    alignment,
    offset,
    padding
  )

/**
 * Popup by which a [SearchTextField] that presents results for the [query] is displayed.
 *
 * This overload is stateless by default and for testing purposes only.
 *
 * @param modifier [Modifier] to be applied to the [SearchTextField].
 * @param query Content to be looked up.
 * @param onQueryChange Lambda invoked whenever the [query] changes.
 * @param resultsLoadable [Profile] results found for [query].
 * @param onDidDismiss Operation performed after this popup is dismissed.
 * @param alignment Positioning relative to the window.
 * @param offset Amount in both axes by which this popup is offset.
 * @param padding Space to surround the [SearchTextField] with.
 * @throws UnsupportedOperationException If the [alignment] is neither predefined nor
 *   bi-dimensional.
 */
@Composable
@InternalTimelineApi
@Throws(UnsupportedOperationException::class)
@VisibleForTesting
fun SearchTextFieldPopup(
  modifier: Modifier = Modifier,
  query: String = "",
  onQueryChange: (query: String) -> Unit = NoOpOnQueryChange,
  resultsLoadable: ListLoadable<ProfileSearchResult> = EmptyResultsLoadable,
  onDidDismiss: () -> Unit = NoOpOnDismissal,
  alignment: Alignment = Alignment.TopStart,
  offset: DpOffset = DpOffset.Zero,
  padding: PaddingValues = Unpadded
) =
  SearchTextFieldPopup(
    query,
    onQueryChange,
    resultsLoadable,
    onDidDismiss,
    modifier,
    alignment,
    offset,
    padding
  )

/**
 * Popup by which a [SearchTextField] that presents results for the [query] is displayed.
 *
 * @param query Content to be looked up.
 * @param onQueryChange Lambda invoked whenever the [query] changes.
 * @param resultsLoadable [Profile] results found for the [query].
 * @param onDidDismiss Operation performed whenever this popup is dismissed.
 * @param modifier [Modifier] to be applied to the [SearchTextField].
 * @param alignment Positioning relative to the window.
 * @param offset Amount in both axes by which this popup is offset.
 * @param padding Space to surround the [SearchTextField] with.
 * @throws UnsupportedOperationException If the [alignment] is neither predefined nor
 *   bi-dimensional.
 */
@Composable
@Throws(UnsupportedOperationException::class)
fun SearchTextFieldPopup(
  query: String,
  onQueryChange: (query: String) -> Unit,
  resultsLoadable: ListLoadable<ProfileSearchResult>,
  onDidDismiss: () -> Unit,
  modifier: Modifier = Modifier,
  alignment: Alignment = Alignment.TopStart,
  offset: DpOffset = DpOffset.Zero,
  padding: PaddingValues = Unpadded
) {
  val context = LocalContext.current
  val compositionContext = rememberCompositionContext()
  val density = LocalDensity.current
  val view = LocalView.current
  val searchTextFieldDefaultShape = SearchTextFieldDefaults.shape
  val popup =
    remember(context, compositionContext, density, view, searchTextFieldDefaultShape) {
      SearchTextFieldPopup(
        WeakReference(context),
        hostViewTreeOwner = ViewTreeOwner.of(view),
        parentCompositionContext = compositionContext,
        density,
        searchTextFieldDefaultShape
      )
    }

  DisposableEffect(alignment) {
    popup.setAlignment(alignment)
    onDispose { popup.setAlignment(Alignment.TopStart) }
  }

  DisposableEffect(offset) {
    popup.setOffset(offset)
    onDispose { popup.setOffset(DpOffset.Zero) }
  }

  DisposableEffect(padding) {
    popup.setPadding(padding)
    onDispose { popup.setPadding(Unpadded) }
  }

  DisposableEffect(onDidDismiss) {
    popup.setOnDidDismissListener(onDidDismiss)
    onDispose { popup.setOnDidDismissListener(null) }
  }

  popup.Content(query, onQueryChange, resultsLoadable, modifier)
}

/**
 * [SearchTextField] that presents results for the [query].
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
  val areResultsLoading = remember(resultsLoadable) { resultsLoadable is ListLoadable.Loading }
  val containsResults = remember(resultsLoadable) { resultsLoadable is ListLoadable.Populated }

  Layout(
    {
      SearchTextField(
        query,
        onQueryChange,
        areResultsLoading,
        modifier,
        RectangleShape,
        elevation = 0.dp
      ) {
        HoverableIconButton(
          onClick = onDismissal,
          Modifier.size(SearchTextFieldDefaults.IconSize).testTag(DismissButtonTag)
        ) {
          Icon(
            AutosTheme.iconography.close.asImageVector,
            contentDescription = stringResource(R.string.composite_timeline_dismiss)
          )
        }
      }

      AnimatedVisibility(
        visible = containsResults,
        enter = slideInVertically(tween(ResultAppearanceAnimationDurationInMilliseconds)),
        exit = slideOutVertically()
      ) {
        LazyColumn {
          item { HorizontalDivider(Modifier.testTag(DividerTag)) }
          resultsLoadable.ifPopulated {
            itemsIndexed(this) { index, result ->
              ResultCard(result, isLastOne = index == lastIndex, Modifier.fillMaxWidth())
            }
          }
        }
      }
    },
    Modifier.shadow(SearchTextFieldDefaults.Elevation, SearchTextFieldDefaults.shape)
      .clip(SearchTextFieldDefaults.shape)
      .wrapContentHeight()
      .semantics(mergeDescendants = true) {}
  ) { measurables, constraints ->
    val searchTextFieldPlaceable = measurables.first().measure(constraints)
    val width = searchTextFieldPlaceable.width
    val resultCardsConstraints = constraints.copy(minWidth = width, maxWidth = width)
    val resultCardsPlaceable = measurables.getOrNull(1)?.measure(resultCardsConstraints)
    layout(width, height = searchTextFieldPlaceable.height + (resultCardsPlaceable?.height ?: 0)) {
      resultCardsPlaceable?.place(x = 0, y = searchTextFieldPlaceable.height)
      searchTextFieldPlaceable.place(x = 0, y = 0)
    }
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

/** Preview of a [SearchTextFieldPopup] with loading results. */
@Composable
@MultiThemePreview
private fun SearchTextFieldPopupLoadingResultsPreview() = AutosTheme {
  SearchTextFieldPopup(resultsLoadable = ListLoadable.Loading())
}

/** Preview of a [SearchTextFieldPopup] without results. */
@Composable
@MultiThemePreview
private fun SearchTextFieldPopupWithoutResultsPreview() = AutosTheme { SearchTextFieldPopup() }

/** Preview of a [SearchTextFieldPopup] with results. */
@Composable
@MultiThemePreview
private fun SearchTextFieldPopupWithResultsPreview() = AutosTheme {
  SearchTextFieldPopup(
    query = "${Account.sample.username}",
    resultsLoadable = serializableListOf(ProfileSearchResult.sample).toListLoadable()
  )
}
