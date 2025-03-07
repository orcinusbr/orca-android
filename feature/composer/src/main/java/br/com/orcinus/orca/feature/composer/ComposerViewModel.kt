/*
 * Copyright © 2023–2025 Orcinus
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

package br.com.orcinus.orca.feature.composer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.orcinus.orca.std.func.monad.Maybe
import br.com.orcinus.orca.std.image.android.AndroidImageLoader
import br.com.orcinus.orca.std.markdown.Markdown
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.MutableStateFlow

internal class ComposerViewModel
private constructor(val avatarLoaderDeferredResult: Deferred<Maybe<*, AndroidImageLoader<*>>>) :
  ViewModel() {
  private val textFlow = MutableStateFlow(Markdown.empty)

  fun setText(text: Markdown) {
    textFlow.value = text
  }

  fun compose() {}

  companion object {
    @JvmStatic
    fun createFactory(avatarLoaderDeferred: Deferred<Maybe<*, AndroidImageLoader<*>>>) =
      viewModelFactory {
        initializer { ComposerViewModel(avatarLoaderDeferred) }
      }
  }
}
