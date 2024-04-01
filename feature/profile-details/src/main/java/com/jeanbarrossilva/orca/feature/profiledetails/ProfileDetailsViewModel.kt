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

package com.jeanbarrossilva.orca.feature.profiledetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.loadable.flow.loadable
import com.jeanbarrossilva.loadable.list.flow.listLoadable
import com.jeanbarrossilva.orca.composite.timeline.post.PostPreview
import com.jeanbarrossilva.orca.composite.timeline.post.figure.gallery.disposition.Disposition
import com.jeanbarrossilva.orca.composite.timeline.post.toPostPreviewFlow
import com.jeanbarrossilva.orca.core.feed.profile.ProfileProvider
import com.jeanbarrossilva.orca.core.feed.profile.post.provider.PostProvider
import com.jeanbarrossilva.orca.ext.coroutines.await
import com.jeanbarrossilva.orca.ext.coroutines.flatMapEach
import com.jeanbarrossilva.orca.ext.coroutines.notifier.notifierFlow
import com.jeanbarrossilva.orca.ext.coroutines.notifier.notify
import com.jeanbarrossilva.orca.ext.intents.share
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import java.net.URL
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

internal class ProfileDetailsViewModel
private constructor(
  application: Application,
  private val profileProvider: ProfileProvider,
  private val postProvider: PostProvider,
  coroutineDispatcher: CoroutineDispatcher,
  private val id: String,
  private val onLinkClick: (URL) -> Unit,
  private val onThumbnailClickListener: Disposition.OnThumbnailClickListener
) : AndroidViewModel(application) {
  private val coroutineScope = viewModelScope + coroutineDispatcher
  private val profileNotifierFlow = notifierFlow()

  @OptIn(ExperimentalCoroutinesApi::class)
  private val profileFlow =
    profileNotifierFlow.flatMapLatest { profileProvider.provide(id).filterNotNull() }

  private val postsIndexFlow = MutableStateFlow(0)

  private val colors
    get() = AutosTheme.getColors(application)

  @get:JvmName("_getApplication")
  private val application
    get() = getApplication<Application>()

  val detailsLoadableFlow =
    profileFlow.map { it.toProfileDetails(coroutineScope, colors) }.loadable(coroutineScope)

  @OptIn(ExperimentalCoroutinesApi::class)
  val postPreviewsLoadableFlow =
    postsIndexFlow
      .flatMapConcat(::getPostPreviewsAt)
      .listLoadable(coroutineScope, SharingStarted.WhileSubscribed())

  fun requestRefresh(onRefresh: () -> Unit = {}) {
    viewModelScope.launch {
      profileNotifierFlow.notify()
      profileFlow.await()
      onRefresh()
    }
  }

  fun share(url: URL) {
    application.share("$url")
  }

  fun favorite(postID: String) {
    coroutineScope.launch { postProvider.provide(postID).first().favorite.toggle() }
  }

  fun repost(postID: String) {
    coroutineScope.launch { postProvider.provide(postID).first().repost.toggle() }
  }

  fun loadPostsAt(index: Int) {
    postsIndexFlow.value = index
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  private fun getPostPreviewsAt(index: Int): Flow<List<PostPreview>> {
    return profileFlow.filterNotNull().flatMapConcat { profile ->
      profile.getPosts(index).flatMapEach { post ->
        post.toPostPreviewFlow(colors, onLinkClick, onThumbnailClickListener)
      }
    }
  }

  companion object {
    fun createFactory(
      application: Application,
      profileProvider: ProfileProvider,
      postProvider: PostProvider,
      id: String,
      onLinkClick: (URL) -> Unit,
      onThumbnailClickListener: Disposition.OnThumbnailClickListener
    ): ViewModelProvider.Factory {
      return viewModelFactory {
        addInitializer(ProfileDetailsViewModel::class) {
          ProfileDetailsViewModel(
            application,
            profileProvider,
            postProvider,
            Dispatchers.Main.immediate,
            id,
            onLinkClick,
            onThumbnailClickListener
          )
        }
      }
    }
  }
}
