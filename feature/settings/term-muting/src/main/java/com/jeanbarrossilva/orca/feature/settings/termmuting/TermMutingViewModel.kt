/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.feature.settings.termmuting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.orca.core.feed.profile.post.content.TermMuter
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
