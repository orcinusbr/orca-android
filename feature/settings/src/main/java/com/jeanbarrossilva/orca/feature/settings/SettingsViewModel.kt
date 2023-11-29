package com.jeanbarrossilva.orca.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.orca.core.feed.profile.post.content.TermMuter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class SettingsViewModel private constructor(private val termMuter: TermMuter) :
  ViewModel() {
  val mutedTermsFlow =
    termMuter
      .getTerms()
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), initialValue = emptyList())

  fun unmute(term: String) {
    viewModelScope.launch { termMuter.unmute(term) }
  }

  companion object {
    fun createFactory(termMuter: TermMuter): ViewModelProvider.Factory {
      return viewModelFactory { initializer { SettingsViewModel(termMuter) } }
    }
  }
}
