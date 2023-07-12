package com.jeanbarrossilva.mastodonte.feature.profiledetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.loadable.flow.loadable
import com.jeanbarrossilva.loadable.list.SerializableList
import com.jeanbarrossilva.loadable.list.flow.listLoadable
import com.jeanbarrossilva.loadable.list.serialize
import com.jeanbarrossilva.mastodonte.core.profile.ProfileProvider
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import com.jeanbarrossilva.mastodonte.core.profile.toot.TootProvider
import com.jeanbarrossilva.mastodonte.feature.profiledetails.toProfileDetails
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

internal class ProfileDetailsViewModel(
    private val contextProvider: ContextProvider,
    private val profileProvider: ProfileProvider,
    private val tootProvider: TootProvider,
    coroutineDispatcher: CoroutineDispatcher,
    private val id: String
) : ViewModel() {
    private val coroutineScope = viewModelScope + coroutineDispatcher
    private val profileFlow = flow { emitAll(profileProvider.provide(id).filterNotNull()) }
    private val tootsIndexFlow = MutableStateFlow(0)

    val detailsLoadableFlow =
        profileFlow.map { it.toProfileDetails(coroutineScope) }.loadable(coroutineScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val tootsLoadableFlow = tootsIndexFlow
        .flatMapConcat { getTootsAt(it) }
        .listLoadable(coroutineScope, SharingStarted.WhileSubscribed())

    fun share(url: URL) {
        contextProvider.provide().share("$url")
    }

    fun favorite(tootID: String) {
        coroutineScope.launch {
            tootProvider.provide(tootID).first().toggleFavorite()
        }
    }

    fun reblog(tootID: String) {
    }

    fun loadTootsAt(index: Int) {
        tootsIndexFlow.value = index
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getTootsAt(index: Int): Flow<SerializableList<Toot>> {
        return profileFlow.filterNotNull().flatMapConcat {
            it.getToots(index).map(List<Toot>::serialize)
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
