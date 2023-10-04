package com.jeanbarrossilva.orca.feature.tootdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.loadable.flow.loadable
import com.jeanbarrossilva.loadable.list.flow.listLoadable
import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.feature.tootdetails.toTootDetailsFlow
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.toTootPreviewFlow
import com.jeanbarrossilva.orca.platform.ui.core.context.ContextProvider
import com.jeanbarrossilva.orca.platform.ui.core.context.share
import com.jeanbarrossilva.orca.platform.ui.core.flatMapEach
import java.net.URL
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val detailsLoadableFlow =
        tootFlow.flatMapLatest { it.toTootDetailsFlow(colors) }.loadable(viewModelScope)

    val commentsLoadableFlow =
        flatMapCombine(commentsIndexFlow, tootFlow) { commentsIndex, toot ->
            toot.comment.get(commentsIndex).flatMapEach {
                it.toTootPreviewFlow(colors)
            }
        }
            .listLoadable(viewModelScope, SharingStarted.WhileSubscribed())

    fun favorite(id: String) {
        viewModelScope.launch {
            tootProvider.provide(id).first().favorite.toggle()
        }
    }

    fun reblog(id: String) {
        viewModelScope.launch {
            tootProvider.provide(id).first().reblog.toggle()
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
