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

package br.com.orcinus.orca.feature.feed

import android.app.Application
import androidx.annotation.VisibleForTesting
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
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearchResult
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearcher
import br.com.orcinus.orca.ext.coroutines.await
import br.com.orcinus.orca.ext.coroutines.flatMapEach
import br.com.orcinus.orca.ext.coroutines.notifier.notifierFlow
import br.com.orcinus.orca.ext.coroutines.notifier.notify
import br.com.orcinus.orca.ext.coroutines.pagination.paginate
import br.com.orcinus.orca.ext.intents.share
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.loadable.list.flow.listLoadableFlow
import com.jeanbarrossilva.loadable.list.toListLoadable
import com.jeanbarrossilva.loadable.list.toSerializableList
import java.net.URI
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

internal class FeedViewModel
@VisibleForTesting
constructor(
  application: Application,
  coroutineContext: CoroutineContext,
  private val profileSearcher: ProfileSearcher,
  private val feedProvider: FeedProvider,
  private val postProvider: PostProvider,
  private val userID: String,
  private val onLinkClick: (URI) -> Unit,
  private val onThumbnailClickListener: Disposition.OnThumbnailClickListener
) : AndroidViewModel(application) {
  private val scope = viewModelScope + coroutineContext
  private val searchQueryMutableFlow = MutableStateFlow("")
  private val indexFlow = MutableStateFlow(0)
  private val postPreviewsLoadableNotifierFlow = notifierFlow()
  private val colors by lazy { AutosTheme.getColors(application) }

  @get:JvmName("_getApplication")
  private val application
    get() = getApplication<Application>()

  val searchQueryFlow = searchQueryMutableFlow.asStateFlow()

  @OptIn(ExperimentalCoroutinesApi::class)
  val searchResultsLoadableFlow = listLoadableFlow {
    searchQueryFlow
      .flatMapMerge(transform = profileSearcher::search)
      .map(List<ProfileSearchResult>::toSerializableList)
      .collect(::load)
  }

  val postPreviewsLoadableFlow =
    indexFlow
      .combine(postPreviewsLoadableNotifierFlow) { index, _ -> index }
      .paginate { feedProvider.provide(userID, page = it) }
      .flatMapEach(selector = PostPreview::id) {
        it.toPostPreviewFlow(colors, onLinkClick, onThumbnailClickListener)
      }
      .map { it.toSerializableList().toListLoadable() }

  private constructor(
    application: Application,
    profileSearcher: ProfileSearcher,
    feedProvider: FeedProvider,
    postProvider: PostProvider,
    userID: String,
    onLinkClick: (URI) -> Unit,
    onThumbnailClickListener: Disposition.OnThumbnailClickListener
  ) : this(
    application,
    EmptyCoroutineContext,
    profileSearcher,
    feedProvider,
    postProvider,
    userID,
    onLinkClick,
    onThumbnailClickListener
  )

  fun search(query: String) {
    searchQueryMutableFlow.value = query
  }

  fun requestRefresh(onRefresh: () -> Unit) {
    postPreviewsLoadableNotifierFlow.notify()
    scope.launch {
      postPreviewsLoadableFlow.await()
      onRefresh()
    }
  }

  fun favorite(postID: String) {
    scope.launch { postProvider.provide(postID).first().favorite.toggle() }
  }

  fun repost(postID: String) {
    scope.launch { postProvider.provide(postID).first().repost.toggle() }
  }

  fun share(uri: URI) {
    application.share("$uri")
  }

  fun loadPostsAt(index: Int) {
    indexFlow.value = index
  }

  companion object {
    fun createFactory(
      application: Application,
      profileSearcher: ProfileSearcher,
      feedProvider: FeedProvider,
      postProvider: PostProvider,
      userID: String,
      onLinkClick: (URI) -> Unit,
      onThumbnailClickListener: Disposition.OnThumbnailClickListener
    ): ViewModelProvider.Factory {
      return viewModelFactory {
        initializer {
          FeedViewModel(
            application,
            profileSearcher,
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
