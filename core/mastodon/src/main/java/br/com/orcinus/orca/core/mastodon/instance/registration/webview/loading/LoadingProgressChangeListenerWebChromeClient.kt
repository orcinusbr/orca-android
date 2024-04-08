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

package br.com.orcinus.orca.core.mastodon.instance.registration.webview.loading

import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.annotation.FloatRange

/**
 * [WebChromeClient] that notifies the [listener] of changes in the loading progress of a [WebView].
 *
 * @param listener [OnLoadingProgressChangeListener] to be notified.
 */
internal class LoadingProgressChangeListenerWebChromeClient(
  private val listener: OnLoadingProgressChangeListener
) : WebChromeClient() {
  /**
   * Listens to changes in loading progress.
   *
   * @see onLoadingProgressChange
   */
  fun interface OnLoadingProgressChangeListener {
    /**
     * Callback that is called whenever the loading progress of a [WebView] changes.
     *
     * @param loadingProgress [Float] between `0f` and `1f` indicating the current loading progress.
     */
    fun onLoadingProgressChange(@FloatRange(from = 0.0, to = 1.0) loadingProgress: Float)
  }

  override fun onProgressChanged(view: WebView?, newProgress: Int) {
    super.onProgressChanged(view, newProgress)
    listener.onLoadingProgressChange(newProgress / 100f)
  }
}
