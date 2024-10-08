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

package br.com.orcinus.orca.feature.gallery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.orcinus.orca.composite.timeline.stat.details.asStatsDetailsFlow
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.PostProvider
import br.com.orcinus.orca.ext.intents.share
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class GalleryViewModel
private constructor(
  application: Application,
  private val postProvider: PostProvider,
  internal val postID: String
) : AndroidViewModel(application) {
  private val postFlow = flow { emitAll(postProvider.provide(postID)) }

  @OptIn(ExperimentalCoroutinesApi::class)
  internal val statsDetailsFlow = postFlow.flatMapLatest(Post::asStatsDetailsFlow)

  internal fun download() {}

  internal fun toggleFavorite() {
    viewModelScope.launch { getPost().favorite.toggle() }
  }

  internal fun toggleRepost() {
    viewModelScope.launch { getPost().repost.toggle() }
  }

  internal fun share() {
    viewModelScope.launch { getApplication<Application>().share("${getPost().uri}") }
  }

  private suspend fun getPost(): Post {
    return postFlow.first()
  }

  companion object {
    fun createFactory(
      application: Application,
      postProvider: PostProvider,
      postID: String
    ): ViewModelProvider.Factory {
      return viewModelFactory {
        initializer { GalleryViewModel(application, postProvider, postID) }
      }
    }
  }
}
