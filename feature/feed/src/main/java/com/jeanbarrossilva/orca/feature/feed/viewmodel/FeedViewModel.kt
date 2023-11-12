package com.jeanbarrossilva.orca.feature.feed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.loadable.list.flow.listLoadable
import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TootPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.toTootPreviewFlow
import com.jeanbarrossilva.orca.platform.ui.core.context.ContextProvider
import com.jeanbarrossilva.orca.platform.ui.core.context.share
import com.jeanbarrossilva.orca.platform.ui.core.flatMapEach
import java.net.URL
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.launch

internal class FeedViewModel(
  private val contextProvider: ContextProvider,
  private val feedProvider: FeedProvider,
  private val tootProvider: TootProvider,
  private val userID: String
) : ViewModel() {
  private val indexFlow = MutableStateFlow(0)
  private val colors by lazy { Colors.getDefault(context) }

  private val context
    get() = contextProvider.provide()

  @OptIn(ExperimentalCoroutinesApi::class)
  val tootPreviewsLoadableFlow =
    indexFlow
      .flatMapLatest { feedProvider.provide(userID, it) }
      .runningFold(emptyList<Toot>()) { accumulator, toots -> accumulator + toots }
      .flatMapEach(selector = TootPreview::id) { it.toTootPreviewFlow(colors) }
      .listLoadable(viewModelScope, SharingStarted.WhileSubscribed())

  fun favorite(tootID: String) {
    viewModelScope.launch { tootProvider.provide(tootID).first().favorite.toggle() }
  }

  fun reblog(tootID: String) {
    viewModelScope.launch { tootProvider.provide(tootID).first().reblog.toggle() }
  }

  fun share(url: URL) {
    context.share("$url")
  }

  fun loadTootsAt(index: Int) {
    indexFlow.value = index
  }

  companion object {
    fun createFactory(
      contextProvider: ContextProvider,
      feedProvider: FeedProvider,
      tootProvider: TootProvider,
      userID: String
    ): ViewModelProvider.Factory {
      return viewModelFactory {
        initializer { FeedViewModel(contextProvider, feedProvider, tootProvider, userID) }
      }
    }
  }
}
