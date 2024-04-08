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
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.instance.registration.Credentials
import br.com.orcinus.orca.core.instance.registration.Registrar

/**
 * [Registrar] that attempts to register an [Account] by directly and automatically interacting with
 * their webpages.
 *
 * @param context [Context] in which the [MastodonRegistrationActivity] that's responsible for
 *   displaying the webpage to be interacted with will be started.
 */
internal class MastodonRegistrar(private val context: Context) : Registrar() {
  override val domains = MastodonRegistrationWebpageInteractorProvider.domains

  override suspend fun register(credentials: Credentials, domain: Domain): Boolean {
    return MastodonRegistrationWebpageInteractorProvider.at(domain)
      .provide()
      .interact(context, credentials)
  }
}
