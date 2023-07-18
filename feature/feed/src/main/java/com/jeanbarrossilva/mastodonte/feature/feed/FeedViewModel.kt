package com.jeanbarrossilva.mastodonte.feature.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import com.jeanbarrossilva.mastodonte.core.profile.toot.TootProvider
import com.jeanbarrossilva.mastodonte.platform.ui.core.context.ContextProvider
import com.jeanbarrossilva.mastodonte.platform.ui.core.context.share
import java.net.URL
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

internal class FeedViewModel(
    private val contextProvider: ContextProvider,
    private val tootProvider: TootProvider
) : ViewModel() {
    private val indexMutableFlow = MutableStateFlow(0)

    val tootsLoadableFlow = MutableStateFlow<ListLoadable<Toot>>(ListLoadable.Loading())

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
}
