/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.feature.postdetails.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.loadable.flow.loadable
import com.jeanbarrossilva.loadable.list.flow.listLoadable
import com.jeanbarrossilva.orca.composite.timeline.post.figure.gallery.disposition.Disposition
import com.jeanbarrossilva.orca.composite.timeline.post.toPostPreviewFlow
import com.jeanbarrossilva.orca.core.feed.profile.post.PostProvider
import com.jeanbarrossilva.orca.ext.coroutines.await
import com.jeanbarrossilva.orca.ext.coroutines.flatMapEach
import com.jeanbarrossilva.orca.ext.coroutines.notifier.notifierFlow
import com.jeanbarrossilva.orca.ext.coroutines.notifier.notify
import com.jeanbarrossilva.orca.ext.intents.share
import com.jeanbarrossilva.orca.feature.postdetails.toPostDetailsFlow
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import java.net.URL
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

internal class PostDetailsViewModel
private constructor(
  application: Application,
  private val postProvider: PostProvider,
  private val id: String,
  private val onLinkClick: (URL) -> Unit,
  private val onThumbnailClickListener: Disposition.OnThumbnailClickListener
) : AndroidViewModel(application) {
  private val notifierFlow = notifierFlow()

  @OptIn(ExperimentalCoroutinesApi::class)
  private val postFlow = notifierFlow.flatMapLatest { postProvider.provide(id) }

  private val commentsIndexFlow = MutableStateFlow(0)

  private val colors
    get() = AutosTheme.getColors(application)

  @get:JvmName("_getApplication")
  private val application
    get() = getApplication<Application>()

  @OptIn(ExperimentalCoroutinesApi::class)
  val detailsLoadableFlow =
    postFlow
      .flatMapLatest { it.toPostDetailsFlow(colors, onLinkClick, onThumbnailClickListener) }
      .loadable(viewModelScope)

  val commentsLoadableFlow =
    flatMapCombine(commentsIndexFlow, postFlow) { commentsIndex, post ->
        post.comment.get(commentsIndex).flatMapEach {
          it.toPostPreviewFlow(colors, onLinkClick, onThumbnailClickListener)
        }
      }
      .listLoadable(viewModelScope, SharingStarted.WhileSubscribed())

  fun requestRefresh(onRefresh: () -> Unit) {
    viewModelScope.launch {
      notifierFlow.notify()
      postFlow.await()
      onRefresh()
    }
  }

  fun favorite(id: String) {
    viewModelScope.launch { postProvider.provide(id).first().favorite.toggle() }
  }

  fun repost(id: String) {
    viewModelScope.launch { postProvider.provide(id).first().repost.toggle() }
  }

  fun share(url: URL) {
    application.share("$url")
  }

  fun loadCommentsAt(index: Int) {
    commentsIndexFlow.value = index
  }

  companion object {
    fun createFactory(
      application: Application,
      postProvider: PostProvider,
      id: String,
      onLinkClick: (URL) -> Unit,
      onThumbnailClickListener: Disposition.OnThumbnailClickListener
    ): ViewModelProvider.Factory {
      return viewModelFactory {
        addInitializer(PostDetailsViewModel::class) {
          PostDetailsViewModel(application, postProvider, id, onLinkClick, onThumbnailClickListener)
        }
      }
    }
  }
}
