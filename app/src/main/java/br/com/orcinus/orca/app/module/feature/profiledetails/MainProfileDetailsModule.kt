/*
 * Copyright © 2023–2025 Orcinus
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

package br.com.orcinus.orca.app.module.feature.profiledetails

import android.content.Context
import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.mastodon.feed.profile.MastodonProfileProvider
import br.com.orcinus.orca.core.mastodon.feed.profile.type.followable.MastodonFollowService
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.module.instanceProvider
import br.com.orcinus.orca.feature.profiledetails.ProfileDetailsModule
import br.com.orcinus.orca.std.injector.Injector
import br.com.orcinus.orca.std.injector.module.injection.lazyInjectionOf

internal class MainProfileDetailsModule(context: Context) :
  ProfileDetailsModule(
    lazyInjectionOf { profileProvider },
    lazyInjectionOf {
      MastodonFollowService(
        context,
        Injector.get<Requester<AuthenticationLock.FailedAuthenticationException>>(),
        profileProvider
      )
    },
    lazyInjectionOf { Injector.from<CoreModule>().instanceProvider().provide().postProvider },
    lazyInjectionOf { MainProfileDetailsBoundary(context) }
  ) {
  companion object {
    private val profileProvider
      get() =
        Injector.from<CoreModule>().instanceProvider().provide().profileProvider
          as MastodonProfileProvider
  }
}
