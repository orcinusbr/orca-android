package com.jeanbarrossilva.orca.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.loadable.flow.loadableFlow
import com.jeanbarrossilva.loadable.list.toSerializableList
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class SearchViewModel private constructor(private val searcher: ProfileSearcher) :
  ViewModel() {
  private val queryMutableFlow = MutableStateFlow("")

  val queryFlow = queryMutableFlow.asStateFlow()

  @OptIn(ExperimentalCoroutinesApi::class)
  val resultsFlow =
    loadableFlow(viewModelScope) {
      queryFlow
        .flatMapLatest(searcher::search)
        .map(List<ProfileSearchResult>::toSerializableList)
        .collect(::load)
    }

  fun setQuery(query: String) {
    queryMutableFlow.value = query
  }

  companion object {
    fun createFactory(searcher: ProfileSearcher): ViewModelProvider.Factory {
      return viewModelFactory { initializer { SearchViewModel(searcher) } }
    }
  }
}
