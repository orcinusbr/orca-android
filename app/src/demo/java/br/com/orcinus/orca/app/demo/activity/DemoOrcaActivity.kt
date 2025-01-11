/*
 * Copyright © 2024–2025 Orcinus
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

package br.com.orcinus.orca.app.demo.activity

import br.com.orcinus.orca.app.activity.OrcaActivity
import br.com.orcinus.orca.app.demo.module.core.DemoCoreModule
import br.com.orcinus.orca.app.demo.module.feature.profiledetails.DemoProfileDetailsModule
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.sample.auth.actor.SampleActorProvider
import br.com.orcinus.orca.core.sample.auth.actor.createSample
import br.com.orcinus.orca.core.sample.image.AuthorImageSource
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.platform.core.image.createSample
import br.com.orcinus.orca.std.image.android.AndroidImageLoader

/**
 * [OrcaActivity] whose [CoreModule] is a demo one which has structures that store and read data
 * from memory, containing sample [Profile]s and [Post]s by default that can be interacted with in
 * order to demonstrate the overall behavior and functionality of the application.
 *
 * @see CoreModule
 */
internal class DemoOrcaActivity : OrcaActivity<DemoCoreModule>() {
  /** Provider of sample [AndroidImageLoader]s for loading avatars, covers and other images. */
  private val imageLoaderProvider by lazy { AndroidImageLoader.Provider.createSample(this) }

  override fun createCoreModule() =
    DemoCoreModule(
      SampleInstance.Builder.create(
          actorProvider =
            SampleActorProvider(
              Actor.Authenticated.createSample(
                avatarLoader = imageLoaderProvider.provide(AuthorImageSource.Default)
              )
            ),
          imageLoaderProvider = imageLoaderProvider
        )
        .withDefaultProfiles()
        .withDefaultPosts()
        .build()
    )

  override fun createProfileDetailsModule() = DemoProfileDetailsModule
}
