package com.jeanbarrossilva.mastodon.feature.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.loadable.flow.loadable
import com.jeanbarrossilva.loadable.ifLoaded
import com.jeanbarrossilva.mastodonte.core.profile.ProfileRepository
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class ProfileViewModel private constructor(repository: ProfileRepository, id: String) :
    ViewModel() {
    private val loadableMutableFlow =
        flow { repository.get(id).filterNotNull().collect(::emit) }.loadable().mutableStateIn(
            viewModelScope
        )

    val loadableFlow = loadableMutableFlow.asStateFlow()

    internal fun toggleFollow() {
        loadableFlow.value.ifLoaded {
            viewModelScope.launch {
                toggleFollow()
            }
        }
    }

    companion object {
        fun createFactory(repository: ProfileRepository, id: String): ViewModelProvider.Factory {
            return viewModelFactory {
                addInitializer(ProfileViewModel::class) {
                    ProfileViewModel(repository, id)
                }
            }
        }
    }
}
