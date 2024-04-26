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

import android.content.Context
import android.webkit.WebView
import androidx.annotation.VisibleForTesting
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.instance.registration.Credentials
import br.com.orcinus.orca.std.uri.url.HostedURLBuilder
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Interacts with the registration webpage of an [Instance].
 *
 * This class is primarily intended for testing, since its [onInteraction] callback gets called even
 * if the webpage didn't load successfully (which would make trying to interacting with it a very
 * nonsensical idea). When inheriting, extend [MastodonRegistrationWebpageInteractor] instead.
 */
@VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
internal abstract class MastodonRegistrationWebpageInteractorWithUncertainLoadingFinishing {
  /**
   * [Domain] of the [Instance] in which registration will be attempted to be performed.
   *
   * @see Instance.domain
   */
  protected abstract val domain: Domain

  /**
   * Interacts with the registration webpage by starting a [MastodonRegistrationActivity].
   *
   * @param context [Context] in which a [MastodonRegistrationActivity] is started.
   * @param credentials [Credentials] with which the [Account] is being registered.
   * @see MastodonRegistrationActivity.start
   */
  suspend fun interact(context: Context, credentials: Credentials): Boolean {
    return suspendCoroutine { continuation ->
      MastodonRegistrationActivity.start(
        context,
        domain,
        HostedURLBuilder.from(domain.uri).path("auth").path("sign_up").build()
      ) { activity, webView, hasLoadedSuccessfully ->
        onInteraction(activity, webView, hasLoadedSuccessfully, credentials).also {
          runCatching { continuation.resume(it) }
        }
        activity.finish()
      }
    }
  }

  /**
   * Callback called when interaction with the registration webpage is requested to be performed.
   *
   * @param activity [MastodonRegistrationActivity] that's hosting the [webView].
   * @param webView [WebView] by which the registration webpage has been loaded.
   * @param hasLoadedSuccessfully Whether the webpage was loaded without any errors.
   * @param credentials [Credentials] with which the [Account] is being registered.
   * @return Whether registration has been performed successfully.
   */
  protected abstract suspend fun onInteraction(
    activity: MastodonRegistrationActivity,
    webView: WebView,
    hasLoadedSuccessfully: Boolean,
    credentials: Credentials
  ): Boolean
}
