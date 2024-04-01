/*
 * Copyright © 2023-2024 Orca
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

package com.jeanbarrossilva.orca.feature.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.loadable.list.toListLoadable
import com.jeanbarrossilva.loadable.list.toSerializableList
import com.jeanbarrossilva.orca.composite.timeline.post.PostPreview
import com.jeanbarrossilva.orca.composite.timeline.post.figure.gallery.disposition.Disposition
import com.jeanbarrossilva.orca.composite.timeline.post.toPostPreviewFlow
import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.post.provider.PostProvider
import com.jeanbarrossilva.orca.ext.coroutines.await
import com.jeanbarrossilva.orca.ext.coroutines.flatMapEach
import com.jeanbarrossilva.orca.ext.coroutines.notifier.notifierFlow
import com.jeanbarrossilva.orca.ext.coroutines.notifier.notify
import com.jeanbarrossilva.orca.ext.coroutines.pagination.paginate
import com.jeanbarrossilva.orca.ext.intents.share
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import java.net.URL
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class FeedViewModel(
  application: Application,
  private val feedProvider: FeedProvider,
  private val postProvider: PostProvider,
  private val userID: String,
  private val onLinkClick: (URL) -> Unit,
  private val onThumbnailClickListener: Disposition.OnThumbnailClickListener
) : AndroidViewModel(application) {
  private val indexFlow = MutableStateFlow(0)
  private val postPreviewsLoadableNotifierFlow = notifierFlow()
  private val colors by lazy { AutosTheme.getColors(application) }

  @get:JvmName("_getApplication")
  private val application
    get() = getApplication<Application>()

  val postPreviewsLoadableFlow =
    indexFlow
      .combine(postPreviewsLoadableNotifierFlow) { index, _ -> index }
      .paginate { feedProvider.provide(userID, page = it) }
      .flatMapEach(selector = PostPreview::id) {
        it.toPostPreviewFlow(colors, onLinkClick, onThumbnailClickListener)
      }
      .map { it.toSerializableList().toListLoadable() }

  fun requestRefresh(onRefresh: () -> Unit) {
    postPreviewsLoadableNotifierFlow.notify()
    viewModelScope.launch {
      postPreviewsLoadableFlow.await()
      onRefresh()
    }
  }

  fun favorite(postID: String) {
    viewModelScope.launch { postProvider.provide(postID).first().favorite.toggle() }
  }

  fun repost(postID: String) {
    viewModelScope.launch { postProvider.provide(postID).first().repost.toggle() }
  }

  fun share(url: URL) {
    application.share("$url")
  }

  fun loadPostsAt(index: Int) {
    indexFlow.value = index
  }

  companion object {
    fun createFactory(
      application: Application,
      feedProvider: FeedProvider,
      postProvider: PostProvider,
      userID: String,
      onLinkClick: (URL) -> Unit,
      onThumbnailClickListener: Disposition.OnThumbnailClickListener
    ): ViewModelProvider.Factory {
      return viewModelFactory {
        initializer {
          FeedViewModel(
            application,
            feedProvider,
            postProvider,
            userID,
            onLinkClick,
            onThumbnailClickListener
          )
        }
      }
    }
  }
}
