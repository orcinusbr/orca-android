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

package br.com.orcinus.orca.platform.autos.kit.scaffold.bar.snack.presenter

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import br.com.orcinus.orca.platform.autos.i18n.ReadableThrowable
import br.com.orcinus.orca.platform.autos.overlays.refresh.Refresh
import br.com.orcinus.orca.platform.autos.overlays.refresh.Refreshable
import java.nio.channels.UnresolvedAddressException

/**
 * Presents the error occurred when trying to fetch data in a [Snackbar] in case it was caused by a
 * [ReadableThrowable] or is an [UnresolvedAddressException]; otherwise, throws the underlying
 * [Throwable].
 *
 * @param error Error because of which the data couldn't be retrieved. It is propagated (that is,
 *   the [Throwable] is thrown) if it isn't an [UnresolvedAddressException] and its cause isn't a
 *   [ReadableThrowable].
 * @param refreshListener [Refresh.Listener] to be notified of refreshes.
 * @param snackbarPresenter [SnackbarPresenter] by which a [Snackbar] informing the error is
 *   presented when it is caused by a [ReadableThrowable] or is an [UnresolvedAddressException].
 * @param modifier [Modifier] to be applied to the underlying [Refreshable].
 * @param lazyListState [LazyListState] through which scroll will be observed.
 */
@Composable
fun ErrorPresentation(
  error: Throwable,
  refreshListener: Refresh.Listener,
  snackbarPresenter: SnackbarPresenter,
  modifier: Modifier = Modifier,
  lazyListState: LazyListState = rememberLazyListState()
) {
  Refreshable(Refresh.immediate(refreshListener), modifier) {
    val defaultError = ReadableThrowable.default

    LaunchedEffect(error, snackbarPresenter, defaultError) {
      val cause = error.cause
      if (error is UnresolvedAddressException) {
        snackbarPresenter.presentError(defaultError.localizedMessage)
      } else if (cause is ReadableThrowable) {
        snackbarPresenter.presentError(cause.localizedMessage)
      } else {
        throw error
      }
    }

    LazyColumn(state = lazyListState) {}
  }
}

/**
 * Presents the error occurred when trying to fetch data in a [Snackbar] in case it was caused by a
 * [ReadableThrowable] or is an [UnresolvedAddressException]; otherwise, throws the underlying
 * [Throwable].
 *
 * This overload is stateless by default and is intended for testing purposes only.
 *
 * @param error Error because of which the data couldn't be retrieved. It is propagated (that is,
 *   the [Throwable] is thrown) if it isn't an [UnresolvedAddressException] and its cause isn't a
 *   [ReadableThrowable].
 * @param modifier [Modifier] to be applied to the underlying [Refreshable].
 * @param refreshListener [Refresh.Listener] to be notified of refreshes.
 * @param snackbarPresenter [SnackbarPresenter] by which a [Snackbar] informing the error is
 *   presented when it is caused by a [ReadableThrowable] or is an [UnresolvedAddressException].
 */
@Composable
@VisibleForTesting
internal fun ErrorPresentation(
  error: Throwable,
  modifier: Modifier = Modifier,
  refreshListener: Refresh.Listener = Refresh.Listener.Empty,
  snackbarPresenter: SnackbarPresenter = rememberSnackbarPresenter()
) {
  ErrorPresentation(error, refreshListener, snackbarPresenter, modifier)
}
