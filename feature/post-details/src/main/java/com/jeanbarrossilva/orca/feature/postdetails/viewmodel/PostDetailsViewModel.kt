package com.jeanbarrossilva.orca.feature.postdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.loadable.flow.loadable
import com.jeanbarrossilva.loadable.list.flow.listLoadable
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.PostProvider
import com.jeanbarrossilva.orca.feature.postdetails.toPostDetailsFlow
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.toPostPreviewFlow
import com.jeanbarrossilva.orca.platform.ui.core.context.ContextProvider
import com.jeanbarrossilva.orca.platform.ui.core.context.share
import com.jeanbarrossilva.orca.platform.ui.core.flatMapEach
import java.net.URL
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

internal class PostDetailsViewModel
private constructor(
  private val contextProvider: ContextProvider,
  private val postProvider: PostProvider,
  private val id: String
) : ViewModel() {
  private val postFlow =
    MutableSharedFlow<Post>().apply { viewModelScope.launch { emitAll(providePostFlow()) } }
  private val commentsIndexFlow = MutableStateFlow(0)

  private val colors
    get() = AutosTheme.getColors(context)

  private val context
    get() = contextProvider.provide()

  @OptIn(ExperimentalCoroutinesApi::class)
  val detailsLoadableFlow =
    postFlow.flatMapLatest { it.toPostDetailsFlow(colors) }.loadable(viewModelScope)

  val commentsLoadableFlow =
    flatMapCombine(commentsIndexFlow, postFlow) { commentsIndex, post ->
        post.comment.get(commentsIndex).flatMapEach { it.toPostPreviewFlow(colors) }
      }
      .listLoadable(viewModelScope, SharingStarted.WhileSubscribed())

  fun requestRefresh(onRefresh: () -> Unit) {
    viewModelScope.launch {
      val post = providePostFlow().first()
      postFlow.emit(post)
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
    context.share("$url")
  }

  fun loadCommentsAt(index: Int) {
    commentsIndexFlow.value = index
  }

  private suspend fun providePostFlow(): Flow<Post> {
    return postProvider.provide(id)
  }

  companion object {
    fun createFactory(
      contextProvider: ContextProvider,
      postProvider: PostProvider,
      id: String
    ): ViewModelProvider.Factory {
      return viewModelFactory {
        addInitializer(PostDetailsViewModel::class) {
          PostDetailsViewModel(contextProvider, postProvider, id)
        }
      }
    }
  }
}
