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

package br.com.orcinus.orca.core.mastodon.instance

import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.Authenticator
import br.com.orcinus.orca.core.auth.Authorizer
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.sample.feed.SampleFeedProvider
import br.com.orcinus.orca.core.sample.feed.profile.SampleProfileProvider
import br.com.orcinus.orca.core.sample.feed.profile.post.SamplePostProvider
import br.com.orcinus.orca.core.sample.feed.profile.post.content.SampleTermMuter
import br.com.orcinus.orca.core.sample.feed.profile.search.SampleProfileSearcher
import br.com.orcinus.orca.core.sample.instance.domain.sample
import br.com.orcinus.orca.platform.core.image.sample
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader

/**
 * [MastodonInstance] with sample structures.
 *
 * @param authorizer [Authorizer] with which the user will be authorized.
 */
internal class SampleMastodonInstance(
  authorizer: Authorizer,
  override val authenticator: Authenticator,
  override val authenticationLock: AuthenticationLock<Authenticator>
) : MastodonInstance<Authorizer, Authenticator>(Domain.sample, authorizer) {
  override val profileProvider = SampleProfileProvider()
  override val profileSearcher = SampleProfileSearcher(profileProvider)
  override val postProvider = SamplePostProvider(profileProvider)
  override val feedProvider =
    SampleFeedProvider(
      profileProvider,
      postProvider,
      SampleTermMuter(),
      ComposableImageLoader.Provider.sample
    )
}
