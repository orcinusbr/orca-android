package com.jeanbarrossilva.orca.feature.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.loadable.list.SerializableList
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class FeedViewModel(
    private val contextProvider: ContextProvider,
    private val feedProvider: FeedProvider,
    private val tootProvider: TootProvider,
    private val userID: String
) : ViewModel() {
    private val indexMutableFlow = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val tootPreviewsLoadableFlow = indexMutableFlow
        .flatMapConcat { getTootPreviewsAt(it) }
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
        indexMutableFlow.value = index
    }

    private suspend fun getTootPreviewsAt(index: Int): Flow<SerializableList<TootPreview>> {
        return feedProvider.provide(userID, index).map { it.map(Toot::toTootPreview) }.map(
            List<TootPreview>::toSerializableList
        )
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
