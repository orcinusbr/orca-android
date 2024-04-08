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

package br.com.orcinus.orca.core.mastodon.instance.registration.interactor

import android.webkit.WebView
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.instance.registration.Credentials
import br.com.orcinus.orca.core.mastodon.instance.registration.MastodonRegistrationActivity
import br.com.orcinus.orca.core.mastodon.instance.registration.MastodonRegistrationWebpageInteractor
import br.com.orcinus.orca.core.mastodon.instance.registration.webview.dom.interactor.`if`
import br.com.orcinus.orca.core.mastodon.instance.registration.webview.dom.onDom

/**
 * [MastodonRegistrationWebpageInteractor] that registers an [Account] at
 * [mastodon.social](https://mastodon.social).
 */
internal class MastodonSocialRegistrationWebpageInteractor :
  MastodonRegistrationWebpageInteractor() {
  override val domain = Companion.domain

  override suspend fun onInteraction(
    activity: MastodonRegistrationActivity,
    webView: WebView,
    credentials: Credentials
  ): Boolean {
    webView.onDom {
      document.getElementsByClassName("button")[0].click()
      document.getElementById("user_email").setValue(credentials.email)
      document.getElementById("user_password").setValue(credentials.password)
      document.getElementById("user_password_confirmation").setValue(credentials.password)
      document.getElementById("user_agreement").setValue("${true}")
      document.getElementsByTagName("input").forEach {
        `if`({ it.get().type.toLowerCase().isStrictlyEqual("button") }) { it.get().click() }
      }
    }
    return true
  }

  companion object {
    /**
     * [Domain] of the [Instance] in which the [MastodonRegistrationWebpageInteractor] tries to
     * register an [Account].
     *
     * @see Instance.domain
     */
    val domain = Domain("mastodon.social")
  }
}
