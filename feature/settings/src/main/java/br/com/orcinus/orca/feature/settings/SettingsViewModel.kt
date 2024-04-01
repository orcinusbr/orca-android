/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.orcinus.orca.core.feed.profile.post.content.TermMuter
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
