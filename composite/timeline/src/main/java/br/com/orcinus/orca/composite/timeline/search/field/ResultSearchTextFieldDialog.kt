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

/** Default implementation of [ResultSearchTextFieldDialog]. */
private class DefaultResultSearchTextFieldDialog(
  override val context: Context,
  override val hostViewTreeOwner: ViewTreeOwner
) : ResultSearchTextFieldDialog() {
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
internal sealed class ResultSearchTextFieldDialog {
  /**
   * [ComposeView] by which a [ResultSearchTextField] is hosted, whose tree ownership is configured
   * upon a request to show the dialog and deconfigured whenever it is dismissed — both respectively
   * triggered by calls to [show] and [dismiss].
   */
  private val hostView by lazy {
    ComposeView(context).apply {
      setContent {
        AutosTheme {
          Box(
            Modifier.padding(
              start = SearchTextFieldDefaults.spacing,
              top = SearchTextFieldDefaults.spacing,
              end = SearchTextFieldDefaults.spacing,

              /*
               * Dimensions of the shadow cast by the text field are disregarded by the view when it
               * is measured; without the padding below, it gets clipped. Calling
               * setClipChildren(false) is futile in this case, given that the text field itself is
               * a composable rather than a child view.
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

  /** [Context] in which the [delegate] is to be displayed. */
  protected abstract val context: Context

  /** Owner by which the tree of the [hostView] is owned. */
  protected abstract val hostViewTreeOwner: ViewTreeOwner

  /** [Dialog] by which a [ResultSearchTextField] is displayed. */
  protected val delegate by lazy {
    Dialog(context, R.style.Theme_Autos).apply {
      /*
       * setCanceledOnTouchOutside(true) requires its window to wrap its content, since it initially
       * matches its parent's — decor view's — size by default and, thus, no interactions are
       * considered external to it by default.
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
   */
  @Composable
  fun Content(
    query: String,
    onQueryChange: (query: String) -> Unit,
    resultsLoadable: ListLoadable<ProfileSearchResult>,
    modifier: Modifier = Modifier
  ) {
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
    onWillShow()
    hostViewTreeOwner.own(hostView)
    delegate.setContentView(hostView)
    delegate.show()
  }

  /** Dismisses this dialog, by which the [ResultSearchTextField] is displayed. */
  @VisibleForTesting fun dismiss() = delegate.dismiss()

  /**
   * Callback called whenever this dialog is requested to be shown, before it is displayed.
   *
   * @see show
   */
  protected abstract fun onWillShow()

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
 * [ResultSearchTextFieldDialog] owned by the [activity].
 *
 * @property activity [ComponentActivity] that serves as the [Context] and the owner of both the
 *   lifecycle and the underlying [Dialog]. Its [View] tree ownership is configured before the
 *   dialog is shown, and reset whenever dismissed.
 */
@VisibleForTesting
internal class OwnedResultSearchTextFieldDialog(private val activity: ComponentActivity) :
  ResultSearchTextFieldDialog() {
  override val context = activity
  override val hostViewTreeOwner = ViewTreeOwner.from(activity)

  override fun onWillShow() {
    activity.initializeViewTreeOwners()
    delegate.setOwnerActivity(activity)
  }
}

/**
 * Produces a remembered [ResultSearchTextFieldDialog] by which the results of a query can be shown
 * on top of preexisting content. In order for it to actually be displayed, its content should be
 * invoked.
 *
 * @throws IllegalStateException If the tree of the local [View] is not owned.
 * @see ResultSearchTextFieldDialog.Content
 * @see LocalView
 */
@Composable
@Throws(IllegalStateException::class)
internal fun rememberResultSearchTextFieldDialog(): ResultSearchTextFieldDialog {
  val context = LocalContext.current
  val viewTreeOwner = rememberViewTreeOwner()
  return remember(context, viewTreeOwner) {
    DefaultResultSearchTextFieldDialog(
      context,
      hostViewTreeOwner = checkNotNull(viewTreeOwner) { "Local view tree is unowned." }
    )
  }
}
