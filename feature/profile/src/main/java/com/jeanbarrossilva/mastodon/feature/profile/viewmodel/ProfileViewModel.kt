package com.jeanbarrossilva.mastodon.feature.profile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.loadable.flow.loadable
import com.jeanbarrossilva.loadable.ifLoaded
import com.jeanbarrossilva.mastodonte.core.profile.ProfileRepository
import java.net.URL
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class ProfileViewModel private constructor(
    application: Application,
    repository: ProfileRepository,
    id: String
) : AndroidViewModel(application) {
    private val loadableMutableFlow =
        flow { repository.get(id).filterNotNull().collect(::emit) }.loadable().mutableStateIn(
            viewModelScope
        )

    val loadableFlow = loadableMutableFlow.asStateFlow()

    internal fun share(url: URL) {
        getApplication<Application>().share("$url")
    }

    internal fun toggleFollow() {
        loadableFlow.value.ifLoaded {
            viewModelScope.launch {
                toggleFollow()
            }
        }
    }

    internal fun favorite(tootID: String) {
    }

    internal fun reblog(tootID: String) {
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
