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

package br.com.orcinus.orca.core.mastodon.instance.registration

import android.webkit.WebView
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.instance.registration.Credentials

/** Interacts with the registration webpage of an [Instance] when it's been successfully loaded. */
internal abstract class MastodonRegistrationWebpageInteractor :
  MastodonRegistrationWebpageInteractorWithUncertainLoadingFinishing() {
  final override suspend fun onInteraction(
    activity: MastodonRegistrationActivity,
    webView: WebView,
    hasLoadedSuccessfully: Boolean,
    credentials: Credentials
  ): Boolean {
    return hasLoadedSuccessfully && onInteraction(activity, webView, credentials)
  }

  /**
   * Callback called when interaction with the registration webpage is requested to be performed.
   *
   * @param activity [MastodonRegistrationActivity] that's hosting the [webView].
   * @param webView [WebView] by which the registration webpage has been loaded.
   * @param credentials [Credentials] with which the [Account] is being registered.
   * @return Resulting authorization code.
   */
  protected abstract suspend fun onInteraction(
    activity: MastodonRegistrationActivity,
    webView: WebView,
    credentials: Credentials
  ): Boolean
}
