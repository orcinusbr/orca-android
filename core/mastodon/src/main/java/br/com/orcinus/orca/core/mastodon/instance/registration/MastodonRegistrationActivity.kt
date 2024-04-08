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
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import br.com.orcinus.orca.composite.composable.ComposableActivity
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.platform.navigation.extra
import br.com.orcinus.orca.platform.starter.on
import java.net.URL
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * [ComposableActivity] that displays a [CustomTabsIntent]-like user interface for registering an
 * [Account] at a given [Instance].
 */
internal class MastodonRegistrationActivity : ComposableActivity() {
  /**
   * [Domain] of the [Instance] in which registration of an [Account] will be tried.
   *
   * @see Instance.domain
   */
  private val domain by extra<Domain>(DOMAIN_KEY)

  /** [URL] to be loaded by the shown [WebView]. */
  private val url by extra<URL>(URL_KEY)

  /**
   * [MutableSharedFlow] to which the callback for when the webpage has finished loading is emitted.
   */
  private val onWebpageLoadFlow =
    MutableSharedFlow<suspend (WebView, hasLoadedSuccessfully: Boolean) -> Unit>(replay = 1)

  @Composable
  override fun Content() {
    MastodonRegistration(
      domain,
      url,
      onWebpageLoad = { webView, hasLoadedSuccessfully ->
        lifecycleScope.launch { onWebpageLoadFlow.first()(webView, hasLoadedSuccessfully) }
      },
      onSharing = {},
      onPop = ::finish
    )
  }

  companion object {
    /** Key of the [Domain] extra. */
    private const val DOMAIN_KEY = "domain"

    /** Key of the [URL] extra. */
    private const val URL_KEY = "url"

    /**
     * Starts a [MastodonRegistrationActivity].
     *
     * @param context [Context] in which it will be started.
     * @param onWebpageLoad Callback for when the webpage has finished loading.
     */
    fun start(
      context: Context,
      domain: Domain,
      url: URL,
      onWebpageLoad:
        suspend (
          activity: MastodonRegistrationActivity, WebView, hasLoadedSuccessfully: Boolean
        ) -> Unit
    ) {
      context
        .on<MastodonRegistrationActivity>()
        .asNewTask()
        .with(DOMAIN_KEY to domain, URL_KEY to url)
        .start { activity ->
          activity.lifecycleScope.launch {
            activity.onWebpageLoadFlow.emit { webView, hasLoadedSuccessfully ->
              onWebpageLoad(activity, webView, hasLoadedSuccessfully)
            }
          }
        }
    }
  }
}
