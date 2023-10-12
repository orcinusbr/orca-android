package com.jeanbarrossilva.orca.feature.settings.termmuting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.orca.core.feed.profile.toot.muting.TermMuter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class TermMutingViewModel private constructor(private val termMuter: TermMuter) :
  ViewModel() {
  private val termMutableFlow = MutableStateFlow("")

  val termFlow = termMutableFlow.asStateFlow()

  fun setTerm(term: String) {
    termMutableFlow.value = term
  }

  fun mute() {
    viewModelScope.launch { termMuter.mute(termFlow.value) }
  }

  companion object {
    fun createFactory(termMuter: TermMuter): ViewModelProvider.Factory {
      return viewModelFactory { initializer { TermMutingViewModel(termMuter) } }
    }
  }
}
