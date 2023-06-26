package com.jeanbarrossilva.mastodonte.feature.tootdetails.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.loadable.flow.loadableFlow
import com.jeanbarrossilva.loadable.list.flow.listLoadable
import com.jeanbarrossilva.loadable.list.serialize
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import com.jeanbarrossilva.mastodonte.core.profile.toot.TootRepository
import com.jeanbarrossilva.mastodonte.feature.tootdetails.Toot as _Toot
import com.jeanbarrossilva.mastodonte.feature.tootdetails.toDomainToot
import java.net.URL
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class TootDetailsViewModel private constructor(
    application: Application,
    repository: TootRepository,
    id: String
) : AndroidViewModel(application) {
    private val coreTootLoadableFlow = flow { emitAll(repository.get(id).filterNotNull()) }
    private val commentsIndexFlow = MutableStateFlow(0)

    internal val tootLoadableFlow = loadableFlow(viewModelScope) {
        coreTootLoadableFlow.map(Toot::toDomainToot).collect(::load)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    internal val commentsLoadableFlow = commentsIndexFlow
        .flatMapConcat { coreTootLoadableFlow.flatMapToComments(it).map(List<_Toot>::serialize) }
        .listLoadable(viewModelScope, SharingStarted.WhileSubscribed())

    internal fun favorite(id: String) {
    }

    internal fun reblog(id: String) {
    }

    internal fun share(url: URL) {
        getApplication<Application>().share("$url")
    }

    internal fun loadCommentsAt(index: Int) {
        commentsIndexFlow.value = index
    }

    private fun Flow<Toot>.flatMapToComments(commentsPage: Int): Flow<List<_Toot>> {
        @OptIn(ExperimentalCoroutinesApi::class)
        return flatMapConcat {
            getCommentsFlow(it, commentsPage)
        }
    }

    private suspend fun getCommentsFlow(coreToot: Toot, commentsPage: Int): Flow<List<_Toot>> {
        return coreToot.getComments(commentsPage).map {
            it.map(Toot::toDomainToot)
        }
    }

    companion object {
        fun createFactory(application: Application, repository: TootRepository, id: String):
            ViewModelProvider.Factory {
            return viewModelFactory {
                addInitializer(TootDetailsViewModel::class) {
                    TootDetailsViewModel(application, repository, id)
                }
            }
        }
    }
}
