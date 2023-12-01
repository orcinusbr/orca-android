/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
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
