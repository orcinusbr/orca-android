/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.feature.profiledetails.test

import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.ProfileProvider
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.PostProvider
import br.com.orcinus.orca.core.sample.feed.profile.SampleProfileProvider
import br.com.orcinus.orca.core.sample.feed.profile.post.SamplePostProvider
import br.com.orcinus.orca.feature.profiledetails.ProfileDetailsBoundary
import br.com.orcinus.orca.feature.profiledetails.ProfileDetailsModule
import br.com.orcinus.orca.std.injector.module.injection.immediateInjectionOf
import br.com.orcinus.orca.std.injector.module.injection.lazyInjectionOf

/**
 * [ProfileDetailsModule] into which a no-op [ProfileDetailsBoundary] is injected.
 *
 * @param profileProvider [SampleProfileProvider] by which the [Profile] whose details are shown is
 *   provided.
 * @param postProvider [SamplePostProvider] that provides the [Profile]'s [Post]s.
 * @see NoOpProfileDetailsBoundary
 * @see inject
 */
class UnnavigableProfileDetailsModule(
  profileProvider: SampleProfileProvider,
  postProvider: SamplePostProvider
) :
  ProfileDetailsModule(
    immediateInjectionOf<ProfileProvider>(profileProvider),
    immediateInjectionOf<PostProvider>(postProvider),
    lazyInjectionOf { NoOpProfileDetailsBoundary }
  )
