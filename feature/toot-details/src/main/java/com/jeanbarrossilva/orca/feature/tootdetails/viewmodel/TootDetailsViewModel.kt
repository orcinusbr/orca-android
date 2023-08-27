package com.jeanbarrossilva.orca.feature.tootdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.loadable.flow.loadable
import com.jeanbarrossilva.loadable.list.flow.listLoadable
import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.feature.tootdetails.toTootDetails
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.toTootPreview
import com.jeanbarrossilva.orca.platform.ui.core.context.ContextProvider
import com.jeanbarrossilva.orca.platform.ui.core.context.share
import com.jeanbarrossilva.orca.platform.ui.core.mapEach
import java.net.URL
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class TootDetailsViewModel private constructor(
    private val contextProvider: ContextProvider,
    private val tootProvider: TootProvider,
    id: String
) : ViewModel() {
    private val tootFlow = flow { emitAll(tootProvider.provide(id)) }
    private val commentsIndexFlow = MutableStateFlow(0)

    private val colors
        get() = Colors.getDefault(context)
    private val context
        get() = contextProvider.provide()

    val detailsLoadableFlow = tootFlow.map { it.toTootDetails(colors) }.loadable(viewModelScope)

    val commentsLoadableFlow =
        flatMapCombine(commentsIndexFlow, tootFlow) { commentsIndex, toot ->
            toot.getComments(commentsIndex).mapEach {
                it.toTootPreview(colors)
            }
        }
            .listLoadable(viewModelScope, SharingStarted.WhileSubscribed())

    fun favorite(id: String) {
        viewModelScope.launch {
            tootProvider.provide(id).first().toggleFavorite()
        }
    }

    fun reblog(id: String) {
        viewModelScope.launch {
            tootProvider.provide(id).first().toggleReblogged()
        }
    }

    fun share(url: URL) {
        context.share("$url")
    }

    fun loadCommentsAt(index: Int) {
        commentsIndexFlow.value = index
    }

    companion object {
        fun createFactory(contextProvider: ContextProvider, tootProvider: TootProvider, id: String):
            ViewModelProvider.Factory {
            return viewModelFactory {
                addInitializer(TootDetailsViewModel::class) {
                    TootDetailsViewModel(contextProvider, tootProvider, id)
                }
            }
        }
    }
}
