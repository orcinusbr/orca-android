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

package br.com.orcinus.orca.app.activity

import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.module.instanceProvider
import br.com.orcinus.orca.core.sample.auth.SampleAuthenticationLock
import br.com.orcinus.orca.core.sample.auth.SampleAuthenticator
import br.com.orcinus.orca.core.sample.auth.actor.SampleActorProvider
import br.com.orcinus.orca.core.sample.feed.profile.SampleProfileProvider
import br.com.orcinus.orca.core.sample.feed.profile.post.SamplePostProvider
import br.com.orcinus.orca.core.sample.feed.profile.post.content.SampleTermMuter
import br.com.orcinus.orca.core.sample.feed.profile.type.followable.SampleFollowService
import br.com.orcinus.orca.core.sample.instance.SampleInstanceProvider
import br.com.orcinus.orca.feature.profiledetails.test.UnnavigableProfileDetailsModule
import br.com.orcinus.orca.platform.core.image.sample
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader
import br.com.orcinus.orca.std.injector.module.injection.lazyInjectionOf

/** Sample, unnavigable [OrcaActivity]. */
internal class SampleOrcaActivity : OrcaActivity() {
  private inline val profileProvider
    get() = instance.profileProvider as SampleProfileProvider

  private inline val postProvider
    get() = instance.postProvider as SamplePostProvider

  private inline val instance
    get() = coreModule.instanceProvider().provide()

  public override val coreModule =
    CoreModule(
      lazyInjectionOf { SampleInstanceProvider(ComposableImageLoader.Provider.sample) },
      lazyInjectionOf { SampleAuthenticationLock(SampleAuthenticator(), SampleActorProvider()) },
      lazyInjectionOf { SampleTermMuter() }
    )
  public override val profileDetailsModule by lazy {
    UnnavigableProfileDetailsModule(
      profileProvider,
      SampleFollowService(profileProvider),
      postProvider
    )
  }
}
