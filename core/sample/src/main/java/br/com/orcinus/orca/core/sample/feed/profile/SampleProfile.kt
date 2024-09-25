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

import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.post.OwnedPost
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.feed.profile.post.stat.addable.AddableStat
import br.com.orcinus.orca.core.feed.profile.post.stat.toggleable.ToggleableStat
import br.com.orcinus.orca.core.sample.auth.actor.sample
import br.com.orcinus.orca.core.sample.feed.profile.composition.Composer
import br.com.orcinus.orca.core.sample.feed.profile.post.stat.addable.SampleAddableStat
import br.com.orcinus.orca.ext.uri.URIBuilder
import java.time.ZonedDateTime
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * [Profile] whose operations are performed in memory and serves as a sample.
 *
 * @property delegate [Author] whose equivalent information is to be attributed to this [Composer].
 */
internal abstract class SampleProfile(private val delegate: Author) : Composer {
  override val id = delegate.id
  override val account = delegate.account
  override val avatarLoader = delegate.avatarLoader
  override val name = delegate.name
  override val uri = delegate.profileURI

  override val postsFlow = MutableStateFlow(emptyList<Post>())

  /** [Post] whose operations are performed in memory and serves as a sample. */
  private inner class SamplePost(
    override val author: Author,
    override val content: Content,
    override val publicationDateTime: ZonedDateTime
  ) : Post() {
    override val actorProvider = ActorProvider.sample
    override val id = UUID.randomUUID().toString()
    override val favorite: ToggleableStat<Profile> = SampleToggleableStat(this@SampleProfile)
    override val repost: ToggleableStat<Profile> = SampleToggleableStat(this@SampleProfile)
    override val comment: AddableStat<Post> = SampleAddableStat()
    override val uri =
      URIBuilder.url()
        .scheme("https")
        .host("orca.orcinus.com.br")
        .path("app")
        .path("posts")
        .path(id)
        .build()

    /**
     * [ToggleableStat] whose elements are stored and toggled in memory.
     *
     * @param T Element which can be added or retrieved.
     * @property toggle Object that will get added this is enabled or removed when it is disabled.
     */
    private inner class SampleToggleableStat<T>(private val toggle: T) :
      ToggleableStat<T>(isEnabled = false, count = 0) {
      /** [MutableStateFlow] containing the elements. */
      private val elementsFlow = MutableStateFlow(emptyList<T>())

      override fun get(page: Int): StateFlow<List<T>> {
        return elementsFlow
      }

      override suspend fun onSetEnabled(isEnabled: Boolean) {
        if (isEnabled) {
          elementsFlow.value += toggle
        } else {
          elementsFlow.value -= toggle
        }
      }
    }

    override suspend fun toOwnedPost(): OwnedPost {
      return SampleOwnedPost(this)
    }
  }

  /** [OwnedPost] that can be removed from the [Composer]. */
  private inner class SampleOwnedPost(delegate: SamplePost) : OwnedPost(delegate) {
    override suspend fun remove() {
      postsFlow.value.find { post -> post.id == id }?.let { postsFlow.value -= it }
    }
  }

  override fun createPost(
    author: Author,
    content: Content,
    publicationDateTime: ZonedDateTime
  ): Post {
    return SamplePost(author, content, publicationDateTime)
  }
}
