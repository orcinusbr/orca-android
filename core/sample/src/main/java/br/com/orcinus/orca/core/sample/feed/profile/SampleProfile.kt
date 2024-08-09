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

package br.com.orcinus.orca.core.sample.feed.profile

import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.sample.feed.profile.post.SamplePostProvider
import br.com.orcinus.orca.std.markdown.Markdown
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

/** Maximum amount of [Post]s emitted by [SampleProfile.getPosts]. */
const val SAMPLE_POSTS_PER_PAGE = 50

/**
 * [Profile] whose operations are performed in memory and serves as a sample.
 *
 * @property postProvider [SamplePostProvider] by which this [SampleProfile]'s [Post]s will be
 *   provided.
 * @property delegate [Author] whose equivalent information is to be attributed to this
 *   [SampleProfile].
 */
internal data class SampleProfile(
  private val postProvider: SamplePostProvider,
  private val delegate: Author,
  override val bio: Markdown,
  override val followerCount: Int,
  override val followingCount: Int
) : Profile {
  override val id = delegate.id
  override val account = delegate.account
  override val avatarLoader = delegate.avatarLoader
  override val name = delegate.name
  override val uri = delegate.profileURI

  override suspend fun getPosts(page: Int): Flow<List<Post>> {
    return postProvider.provideAllBy(id).filterNotNull().map {
      it.windowed(SAMPLE_POSTS_PER_PAGE, partialWindows = true).getOrElse(page) { emptyList() }
    }
  }
}
