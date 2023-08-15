package com.jeanbarrossilva.orca.feature.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.loadable.list.flow.listLoadable
import com.jeanbarrossilva.loadable.list.toSerializableList
import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TootPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.toTootPreview
import com.jeanbarrossilva.orca.platform.ui.core.context.ContextProvider
import com.jeanbarrossilva.orca.platform.ui.core.context.share
import java.net.URL
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class FeedViewModel(
    private val contextProvider: ContextProvider,
    private val feedProvider: FeedProvider,
    private val tootProvider: TootProvider,
    private val userID: String
) : ViewModel() {
    private val indexFlow = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val tootPreviewsLoadableFlow = indexFlow
        .flatMapLatest { feedProvider.provide(userID, page = it) }
        .map { it.map(Toot::toTootPreview) }
        .map(List<TootPreview>::toSerializableList)
        .listLoadable(viewModelScope, SharingStarted.WhileSubscribed())

    fun favorite(tootID: String) {
        viewModelScope.launch {
            tootProvider.provide(tootID).first().toggleFavorite()
        }
    }

    fun reblog(tootID: String) {
        viewModelScope.launch {
            tootProvider.provide(tootID).first().toggleReblogged()
        }
    }

    fun share(url: URL) {
        contextProvider.provide().share("$url")
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
                initializer {
                    FeedViewModel(contextProvider, feedProvider, tootProvider, userID)
                }
            }
        }
    }
}
