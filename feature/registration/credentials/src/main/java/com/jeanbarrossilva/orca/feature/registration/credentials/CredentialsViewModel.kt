/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.feature.registration.credentials

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class CredentialsViewModel : ViewModel() {
  private val emailMutableFlow = MutableStateFlow("")
  private val passwordMutableFlow = MutableStateFlow("")

  val emailFlow = emailMutableFlow.asStateFlow()
  val passwordFlow = passwordMutableFlow.asStateFlow()

  fun setEmail(email: String) {
    emailMutableFlow.value = email
  }

  fun setPassword(password: String) {
    passwordMutableFlow.value = password
  }

  companion object {
    fun createFactory(): ViewModelProvider.Factory {
      return viewModelFactory { initializer { CredentialsViewModel() } }
    }
  }
}
