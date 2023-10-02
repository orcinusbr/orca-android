package com.jeanbarrossilva.orca.feature.profiledetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.loadable.flow.loadable
import com.jeanbarrossilva.loadable.list.flow.listLoadable
import com.jeanbarrossilva.orca.core.feed.profile.ProfileProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TootPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.toTootPreviewFlow
import com.jeanbarrossilva.orca.platform.ui.core.context.ContextProvider
import com.jeanbarrossilva.orca.platform.ui.core.context.share
import com.jeanbarrossilva.orca.platform.ui.core.flatMapEach
import java.net.URL
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

internal class ProfileDetailsViewModel private constructor(
    private val contextProvider: ContextProvider,
    private val profileProvider: ProfileProvider,
    private val tootProvider: TootProvider,
    coroutineDispatcher: CoroutineDispatcher,
    private val id: String
) : ViewModel() {
    private val coroutineScope = viewModelScope + coroutineDispatcher
    private val profileFlow = flow { emitAll(profileProvider.provide(id).filterNotNull()) }
    private val tootsIndexFlow = MutableStateFlow(0)

    private val context
        get() = contextProvider.provide()

    val detailsLoadableFlow = profileFlow
        .map { it.toProfileDetails(coroutineScope, Colors.getDefault(context)) }
        .loadable(coroutineScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val tootPreviewsLoadableFlow = tootsIndexFlow
        .flatMapConcat(::getTootPreviewsAt)
        .listLoadable(coroutineScope, SharingStarted.WhileSubscribed())

    fun share(url: URL) {
        context.share("$url")
    }

    fun favorite(tootID: String) {
        coroutineScope.launch {
            tootProvider.provide(tootID).first().favorite.toggle()
        }
    }

    fun reblog(tootID: String) {
        coroutineScope.launch {
            tootProvider.provide(tootID).first().reblog.toggle()
        }
    }

    fun loadTootsAt(index: Int) {
        tootsIndexFlow.value = index
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getTootPreviewsAt(index: Int): Flow<List<TootPreview>> {
        return profileFlow.filterNotNull().flatMapConcat { profile ->
            profile.getToots(index).flatMapEach { toot ->
                toot.toTootPreviewFlow(Colors.getDefault(context))
            }
        }
    }

    companion object {
        fun createFactory(
            contextProvider: ContextProvider,
            profileProvider: ProfileProvider,
            tootProvider: TootProvider,
            id: String
        ): ViewModelProvider.Factory {
            return viewModelFactory {
                addInitializer(ProfileDetailsViewModel::class) {
                    ProfileDetailsViewModel(
                        contextProvider,
                        profileProvider,
                        tootProvider,
                        Dispatchers.Main.immediate,
                        id
                    )
                }
            }
        }
    }
}
