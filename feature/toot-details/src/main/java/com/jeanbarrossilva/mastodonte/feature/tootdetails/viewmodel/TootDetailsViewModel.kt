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
import com.jeanbarrossilva.mastodonte.core.profile.toot.TootProvider
import com.jeanbarrossilva.mastodonte.feature.tootdetails.TootDetails
import com.jeanbarrossilva.mastodonte.feature.tootdetails.toTootDetails
import java.net.URL
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class TootDetailsViewModel private constructor(
    application: Application,
    provider: TootProvider,
    id: String
) : AndroidViewModel(application) {
    private val tootFlow = flow { emitAll(provider.provide(id)) }
    private val commentsIndexFlow = MutableStateFlow(0)

    val detailsLoadableFlow =
        loadableFlow(viewModelScope) { tootFlow.map(Toot::toTootDetails).collect(::load) }

    @OptIn(ExperimentalCoroutinesApi::class)
    val commentsLoadableFlow = commentsIndexFlow
        .flatMapConcat { tootFlow.flatMapToComments(it).map(List<TootDetails>::serialize) }
        .listLoadable(viewModelScope, SharingStarted.WhileSubscribed())

    fun favorite() {
        viewModelScope.launch {
            tootFlow.first().toggleFavorite()
        }
    }

    fun reblog() {
        viewModelScope.launch {
            tootFlow.first().toggleReblogged()
        }
    }

    fun share(url: URL) {
        getApplication<Application>().share("$url")
    }

    fun loadCommentsAt(index: Int) {
        commentsIndexFlow.value = index
    }

    private fun Flow<Toot>.flatMapToComments(commentsPage: Int): Flow<List<TootDetails>> {
        @OptIn(ExperimentalCoroutinesApi::class)
        return flatMapConcat {
            getCommentsFlow(it, commentsPage)
        }
    }

    private suspend fun getCommentsFlow(toot: Toot, commentsPage: Int): Flow<List<TootDetails>> {
        return toot.getComments(commentsPage).map {
            it.map(Toot::toTootDetails)
        }
    }

    companion object {
        fun createFactory(application: Application, repository: TootProvider, id: String):
            ViewModelProvider.Factory {
            return viewModelFactory {
                addInitializer(TootDetailsViewModel::class) {
                    TootDetailsViewModel(application, repository, id)
                }
            }
        }
    }
}
