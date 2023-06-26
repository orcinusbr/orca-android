package com.jeanbarrossilva.mastodon.feature.profile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.loadable.flow.loadable
import com.jeanbarrossilva.loadable.ifLoaded
import com.jeanbarrossilva.loadable.list.flow.listLoadable
import com.jeanbarrossilva.loadable.list.serialize
import com.jeanbarrossilva.mastodonte.core.profile.ProfileRepository
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import java.net.URL
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ProfileViewModel private constructor(
    application: Application,
    repository: ProfileRepository,
    id: String
) : AndroidViewModel(application) {
    private val profileFlow = flow { emitAll(repository.get(id).filterNotNull()) }
    private val loadableMutableFlow = profileFlow.loadable().mutableStateIn(viewModelScope)
    private val tootsIndexFlow = MutableStateFlow(0)

    internal val profileLoadableFlow = loadableMutableFlow.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    internal val tootsLoadableFlow = tootsIndexFlow
        .flatMapConcat { tootsIndex ->
            profileFlow.flatMapConcat {
                it.getToots(tootsIndex).map(List<Toot>::serialize)
            }
        }
        .listLoadable(viewModelScope, SharingStarted.WhileSubscribed())

    internal fun share(url: URL) {
        getApplication<Application>().share("$url")
    }

    internal fun toggleFollow() {
        profileLoadableFlow.value.ifLoaded {
            viewModelScope.launch {
                toggleFollow()
            }
        }
    }

    internal fun favorite(tootID: String) {
    }

    internal fun reblog(tootID: String) {
    }

    internal fun loadTootsAt(index: Int) {
        tootsIndexFlow.value = index
    }

    companion object {
        fun createFactory(
            application: Application,
            repository: ProfileRepository,
            id: String
        ): ViewModelProvider.Factory {
            return viewModelFactory {
                addInitializer(ProfileViewModel::class) {
                    ProfileViewModel(application, repository, id)
                }
            }
        }
    }
}
