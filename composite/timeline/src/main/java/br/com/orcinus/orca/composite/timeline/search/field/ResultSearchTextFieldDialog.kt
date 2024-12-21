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
import androidx.activity.ComponentActivity
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.Popup
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearchResult
import br.com.orcinus.orca.platform.autos.R
import br.com.orcinus.orca.platform.autos.kit.input.text.SearchTextFieldDefaults
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.focus.rememberImmediateFocusRequester
import com.jeanbarrossilva.loadable.list.ListLoadable
import java.lang.ref.WeakReference

/** Default implementation of [ResultSearchTextFieldDialog]. */
private class DefaultResultSearchTextFieldDialog
private constructor(
  override val contextRef: WeakReference<Context>,
  override val hostViewTreeOwner: ViewTreeOwner?
) : ResultSearchTextFieldDialog<Context>() {
  constructor(
    context: Context,
    hostViewTreeOwner: ViewTreeOwner?
  ) : this(WeakReference(context), hostViewTreeOwner)

  override fun onWillShow() = Unit
}

/**
 * Manages instantiation, configuration, display, deconfiguration and dismissal of a [Dialog] in
 * which a [ResultSearchTextField] is shown. Acts an alternative to showing a [Popup], since it was
 * the solution that was previously adopted by Orca in `androidx.compose.ui:ui` 1.6.8 and had issues
 * such as inability to have both focusability and dismissibility via an outside click, and negative
 * cursor- and select-handles-Y-offsetting.
 *
 * In order to produce an instance of this class, call [rememberResultSearchTextFieldDialog].
 *
 * For displaying the dialog, invoke the [Content] composable.
 */
internal sealed class ResultSearchTextFieldDialog<C : Context> {
  /** [Modifier] applied to the [ResultSearchTextField]. */
  private var modifier by mutableStateOf<Modifier>(Modifier)

  /** Content being looked up in the [ResultSearchTextField]. */
  private var query by mutableStateOf("")

  /** Lambda invoked whenever the query changes in the [ResultSearchTextField]. */
  private var onQueryChange by mutableStateOf(noOpOnQueryChange)

  /** [Profile] results found by the [query] in the [ResultSearchTextField]. */
  private var resultsLoadable by mutableStateOf(emptyResultsLoadable)

  /**
   * Listener that gets notified after the [delegate] is dismissed.
   *
   * @see Dialog.dismiss
   * @see dismiss
   * @see doOnDidDismiss
   */
  private var onDidDismissListener: (() -> Unit)? = null

  /** [WeakReference] to the [Context] in which the [delegate] is to be displayed. */
  protected abstract val contextRef: WeakReference<C>

  /**
   * Owner by which the tree of the host [View] is owned.
   *
   * @see createHostView
   */
  protected abstract val hostViewTreeOwner: ViewTreeOwner?

  /**
   * [Dialog] by which a [ResultSearchTextField] is displayed; `null` if the [context] has been
   * garbage-collected by the time it is instantiated.
   */
  protected val delegate by lazy {
    context?.let {
      Dialog(it, R.style.Theme_Autos).apply {
        /*
         * setCanceledOnTouchOutside(true) requires its window to wrap its content, since it
         * initially matches its parent's — decor view's — size by default and, thus, no
         * interactions are considered external to it by default.
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
  protected inline val context
    get() = contextRef.get()

  /**
   * Shows this dialog when it enters composition and dismisses it when it is decomposed.
   *
   * This overload is stateless by default and is for testing purposes only.
   *
   * @param query Content to be looked up.
   * @param onQueryChange Lambda invoked whenever the [query] changes.
   * @param resultsLoadable [Profile] results found by the [query].
   * @param modifier [Modifier] to be applied to the [ResultSearchTextField].
   */
  @Composable
  @VisibleForTesting
  fun Content(
    modifier: Modifier = Modifier,
    query: String = "",
    onQueryChange: (query: String) -> Unit = noOpOnQueryChange,
    resultsLoadable: ListLoadable<ProfileSearchResult> = emptyResultsLoadable
  ) = Content(query, onQueryChange, resultsLoadable, modifier)

  /**
   * Shows this dialog when it enters composition and dismisses it when it is decomposed.
   *
   * @param query Content to be looked up.
   * @param onQueryChange Lambda invoked whenever the [query] changes.
   * @param resultsLoadable [Profile] results found by the [query].
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
    DisposableEffect(this) {
      delegate?.let {
        check(!it.isShowing) {
          "Cannot perform simultaneous compositions of a result search text field dialog!"
        }
      }
      onDispose {}
    }

    DisposableEffect(this, modifier) {
      this@ResultSearchTextFieldDialog.modifier = modifier
      onDispose { this@ResultSearchTextFieldDialog.modifier = Modifier }
    }

    DisposableEffect(this, query) {
      this@ResultSearchTextFieldDialog.query = query
      onDispose { this@ResultSearchTextFieldDialog.query = "" }
    }

    DisposableEffect(this, onQueryChange) {
      this@ResultSearchTextFieldDialog.onQueryChange = onQueryChange
      onDispose { this@ResultSearchTextFieldDialog.onQueryChange = noOpOnQueryChange }
    }

    DisposableEffect(this, resultsLoadable) {
      this@ResultSearchTextFieldDialog.resultsLoadable = resultsLoadable
      onDispose { this@ResultSearchTextFieldDialog.resultsLoadable = emptyResultsLoadable }
    }

    DisposableEffect(this) {
      show()
      onDispose(::dismiss)
    }
  }

  /**
   * Schedules the execution of an action for after the dialog has been dismissed.
   *
   * @param listener Listener to be notified of dismissals.
   * @see Dialog.dismiss
   */
  inline fun doOnDidDismiss(crossinline listener: () -> Unit) {
    val previousListener = onDidDismissListener
    onDidDismissListener = {
      previousListener?.invoke()
      listener()
    }
  }

  /** Shows this dialog, by which the [ResultSearchTextField] is displayed. */
  @VisibleForTesting
  fun show() {
    val delegate = delegate
    val hostView = createHostView()
    val hostViewTreeOwner = hostViewTreeOwner
    if (delegate != null && hostView != null && hostViewTreeOwner != null) {
      onWillShow()
      hostViewTreeOwner.own(hostView)
      delegate.setContentView(hostView)
      delegate.show()
    }
  }

  /** Dismisses this dialog, by which the [ResultSearchTextField] is displayed. */
  @VisibleForTesting
  fun dismiss() {
    delegate?.dismiss()
  }

  /**
   * Callback called whenever this dialog is requested to be shown, before it is displayed.
   *
   * @see show
   */
  protected abstract fun onWillShow()

  /**
   * Creates a [ComposeView] by which a [ResultSearchTextField] is hosted, whose tree ownership is
   * configured upon a request to show this dialog and deconfigured whenever it is dismissed — both
   * respectively triggered by calls to [show] and [dismiss].
   *
   * @return The host [View], or `null` in case this method gets called after the [context] has been
   *   garbage-collected.
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

  private companion object {
    /**
     * [ColorDrawable] defined as the [delegate]'s [Window] background for making it transparent.
     */
    @JvmStatic private val transparentDrawable = ColorDrawable(Color.TRANSPARENT)

    /**
     * Default lambda which is a no-op for when the query changes.
     *
     * @see onQueryChange
     */
    @JvmStatic private val noOpOnQueryChange = { _: String -> }

    /**
     * Default [ListLoadable] of query results; an empty one.
     *
     * @see resultsLoadable
     * @see ListLoadable.Empty
     */
    @JvmStatic
    private val emptyResultsLoadable: ListLoadable<ProfileSearchResult> = ListLoadable.Empty()
  }
}

/**
 * [ResultSearchTextFieldDialog] owned by a [ComponentActivity] which serves as the [Context] and
 * the owner of both the host [View] and the underlying [delegate]; whose [View] tree ownership is
 * configured before this dialog is shown.
 *
 * @see createHostView
 * @see show
 */
@VisibleForTesting
internal class OwnedResultSearchTextFieldDialog
private constructor(override val contextRef: WeakReference<ComponentActivity>) :
  ResultSearchTextFieldDialog<ComponentActivity>() {
  override val hostViewTreeOwner by lazy { context?.let(ViewTreeOwner::from) }

  constructor(context: ComponentActivity) : this(WeakReference(context))

  override fun onWillShow() {
    val activity = context
    val delegate = delegate
    if (activity != null && delegate != null) {
      activity.initializeViewTreeOwners()
      delegate.setOwnerActivity(activity)
    }
  }
}

/**
 * Produces a remembered [ResultSearchTextFieldDialog] by which the results of a query can be shown
 * on top of preexisting content. In order for it to actually be displayed, its content should be
 * invoked.
 *
 * @see ResultSearchTextFieldDialog.Content
 * @see LocalView
 */
@Composable
internal fun rememberResultSearchTextFieldDialog(): ResultSearchTextFieldDialog<Context> {
  val context = LocalContext.current
  val viewTreeOwner = rememberViewTreeOwner()
  return remember(context, viewTreeOwner) {
    DefaultResultSearchTextFieldDialog(context, hostViewTreeOwner = viewTreeOwner)
  }
}
