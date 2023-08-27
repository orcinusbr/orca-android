package com.jeanbarrossilva.orca.feature.feed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.loadable.list.flow.listLoadable
import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.toTootPreview
import com.jeanbarrossilva.orca.platform.ui.core.context.ContextProvider
import com.jeanbarrossilva.orca.platform.ui.core.context.share
import com.jeanbarrossilva.orca.platform.ui.core.mapEach
import java.net.URL
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

internal class FeedViewModel(
    private val contextProvider: ContextProvider,
    private val feedProvider: FeedProvider,
    private val tootProvider: TootProvider,
    private val userID: String
) : ViewModel() {
    private val indexFlow = MutableStateFlow(0)

    private val context
        get() = contextProvider.provide()

    @OptIn(ExperimentalCoroutinesApi::class)
    val tootPreviewsLoadableFlow = indexFlow
        .flatMapLatest { feedProvider.provide(userID, it) }
        .mapEach { it.toTootPreview(Colors.getDefault(context)) }
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
                initializer {
                    FeedViewModel(contextProvider, feedProvider, tootProvider, userID)
                }
            }
        }
    }
}
