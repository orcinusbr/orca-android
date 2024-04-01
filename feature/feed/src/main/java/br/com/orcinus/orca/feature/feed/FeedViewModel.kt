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

package br.com.orcinus.orca.feature.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.orcinus.orca.composite.timeline.post.PostPreview
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.disposition.Disposition
import br.com.orcinus.orca.composite.timeline.post.toPostPreviewFlow
import br.com.orcinus.orca.core.feed.FeedProvider
import br.com.orcinus.orca.core.feed.profile.post.provider.PostProvider
import br.com.orcinus.orca.ext.coroutines.await
import br.com.orcinus.orca.ext.coroutines.flatMapEach
import br.com.orcinus.orca.ext.coroutines.notifier.notifierFlow
import br.com.orcinus.orca.ext.coroutines.notifier.notify
import br.com.orcinus.orca.ext.coroutines.pagination.paginate
import br.com.orcinus.orca.ext.intents.share
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.loadable.list.toListLoadable
import com.jeanbarrossilva.loadable.list.toSerializableList
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
