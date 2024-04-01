/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.feature.registration.ongoing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.sample.instance.domain.samples
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.withIndex

internal class OngoingViewModel : ViewModel() {
  val indexedDomainFlow =
    Domain.samples
      .asFlow()
      .withIndex()
      .onEach { (index, _) ->
        if (index > 0) {
          delay(perDomainDelay)
        }
      }
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), initialValue = null)

  companion object {
    val perDomainDelay = 4.seconds

    fun createFactory(): ViewModelProvider.Factory {
      return viewModelFactory { initializer { OngoingViewModel() } }
    }
  }
}
