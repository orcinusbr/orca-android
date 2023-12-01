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

package com.jeanbarrossilva.orca.feature.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.loadable.list.flow.listLoadable
import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.PostProvider
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.PostPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.toPostPreviewFlow
import com.jeanbarrossilva.orca.platform.ui.core.await
import com.jeanbarrossilva.orca.platform.ui.core.context.ContextProvider
import com.jeanbarrossilva.orca.platform.ui.core.context.share
import com.jeanbarrossilva.orca.platform.ui.core.flatMapEach
import java.net.URL
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.launch

internal class FeedViewModel(
  private val contextProvider: ContextProvider,
  private val feedProvider: FeedProvider,
  private val postProvider: PostProvider,
  private val userID: String
) : ViewModel() {
  private val indexFlow = MutableStateFlow<Int?>(0)
  private val colors by lazy { AutosTheme.getColors(context) }

  private val context
    get() = contextProvider.provide()

  @OptIn(ExperimentalCoroutinesApi::class)
  val postPreviewsLoadableFlow =
    indexFlow
      .flatMapLatest { it?.let { index -> feedProvider.provide(userID, index) } ?: flowOf(null) }
      .runningFold(emptyList<Post>()) { accumulator, posts -> accumulator + posts.orEmpty() }
      .flatMapEach(selector = PostPreview::id) { it.toPostPreviewFlow(colors) }
      .listLoadable(viewModelScope, SharingStarted.WhileSubscribed())

  fun requestRefresh(onRefresh: () -> Unit) {
    val index = indexFlow.value
    indexFlow.value = null
    indexFlow.value = index
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
    context.share("$url")
  }

  fun loadPostsAt(index: Int) {
    indexFlow.value = index
  }

  companion object {
    fun createFactory(
      contextProvider: ContextProvider,
      feedProvider: FeedProvider,
      postProvider: PostProvider,
      userID: String
    ): ViewModelProvider.Factory {
      return viewModelFactory {
        initializer { FeedViewModel(contextProvider, feedProvider, postProvider, userID) }
      }
    }
  }
}
