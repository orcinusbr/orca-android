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

import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * [WebViewClient] for specifically listening to when the webpage of a [WebView] has finished
 * loading.
 *
 * @param listener [OnLoadingFinishingListener] to be notified.
 */
internal class LoadingFinishingListenerWebViewClient(
  private val listener: OnLoadingFinishingListener
) : WebViewClient() {
  /**
   * Listener that is notified when a webpage has been loaded.
   *
   * @see onLoadFinishing
   */
  fun interface OnLoadingFinishingListener {
    /**
     * Callback called when the webpage has been loaded.
     *
     * @param hasLoadedSuccessfully Whether the webpage was loaded without any errors.
     */
    fun onLoadFinishing(hasLoadedSuccessfully: Boolean)
  }

  override fun onPageFinished(view: WebView?, url: String?) {
    super.onPageFinished(view, url)
    listener.onLoadFinishing(hasLoadedSuccessfully = true)
  }

  @Suppress("OVERRIDE_DEPRECATION")
  override fun onReceivedError(
    view: WebView?,
    errorCode: Int,
    description: String?,
    failingUrl: String?
  ) {
    @Suppress("DEPRECATION") super.onReceivedError(view, errorCode, description, failingUrl)
    listener.onLoadFinishing(hasLoadedSuccessfully = false)
  }
}
