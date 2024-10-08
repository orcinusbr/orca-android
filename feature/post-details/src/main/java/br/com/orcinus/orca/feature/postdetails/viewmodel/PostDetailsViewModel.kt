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

package br.com.orcinus.orca.feature.postdetails.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.disposition.Disposition
import br.com.orcinus.orca.composite.timeline.post.toPostPreview
import br.com.orcinus.orca.core.feed.profile.post.PostProvider
import br.com.orcinus.orca.ext.coroutines.await
import br.com.orcinus.orca.ext.coroutines.mapEach
import br.com.orcinus.orca.ext.coroutines.notifier.notifierFlow
import br.com.orcinus.orca.ext.coroutines.notifier.notify
import br.com.orcinus.orca.ext.coroutines.pagination.paginate
import br.com.orcinus.orca.ext.intents.share
import br.com.orcinus.orca.feature.postdetails.toPostDetailsFlow
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.loadable.flow.loadable
import com.jeanbarrossilva.loadable.list.flow.listLoadable
import java.net.URI
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
  private val onLinkClick: (URI) -> Unit,
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

  @OptIn(ExperimentalCoroutinesApi::class)
  val commentsLoadableFlow =
    postFlow
      .flatMapLatest { post ->
        commentsIndexFlow.paginate(post.comment::get).mapEach { comment ->
          comment.toPostPreview(colors, onLinkClick, onThumbnailClickListener)
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

  fun share(uri: URI) {
    application.share("$uri")
  }

  fun loadCommentsAt(index: Int) {
    commentsIndexFlow.value = index
  }

  companion object {
    fun createFactory(
      application: Application,
      postProvider: PostProvider,
      id: String,
      onLinkClick: (URI) -> Unit,
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
