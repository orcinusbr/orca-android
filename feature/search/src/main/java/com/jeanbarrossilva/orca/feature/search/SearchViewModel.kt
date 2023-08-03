package com.jeanbarrossilva.orca.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.loadable.list.SerializableList
import com.jeanbarrossilva.loadable.list.toSerializableList
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

internal class SearchViewModel private constructor(private val searcher: ProfileSearcher) :
    ViewModel() {
    private val queryMutableFlow = MutableStateFlow("")

    val queryFlow = queryMutableFlow.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val resultsFlow = queryFlow
        .flatMapLatest(searcher::search)
        .map(List<ProfileSearchResult>::toSerializableList)
        .map<_, Loadable<SerializableList<ProfileSearchResult>>> { Loadable.Loaded(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            initialValue = Loadable.Loading()
        )

    init {
        resultsFlow.onEach { resultsFlow }
    }

    fun setQuery(query: String) {
        queryMutableFlow.value = query
    }

    companion object {
        fun createFactory(searcher: ProfileSearcher): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    SearchViewModel(searcher)
                }
            }
        }
    }
}
