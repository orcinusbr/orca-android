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

package br.com.orcinus.orca.app.demo.module.feature.profiledetails

import br.com.orcinus.orca.app.module.feature.profiledetails.MainProfileDetailsBoundary
import br.com.orcinus.orca.core.feed.profile.ProfileProvider
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.module.instanceProvider
import br.com.orcinus.orca.core.sample.feed.profile.SampleProfileProvider
import br.com.orcinus.orca.core.sample.feed.profile.type.followable.SampleFollowService
import br.com.orcinus.orca.feature.profiledetails.ProfileDetailsModule
import br.com.orcinus.orca.std.injector.Injector
import br.com.orcinus.orca.std.injector.module.injection.lazyInjectionOf

internal object DemoProfileDetailsModule :
  ProfileDetailsModule(
    lazyInjectionOf { Injector.from<CoreModule>().instanceProvider().provide().profileProvider },
    lazyInjectionOf { SampleFollowService(get<ProfileProvider>() as SampleProfileProvider) },
    lazyInjectionOf { Injector.from<CoreModule>().instanceProvider().provide().postProvider },
    lazyInjectionOf { MainProfileDetailsBoundary(context = Injector.get()) }
  )
