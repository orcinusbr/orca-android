/*
 * Copyright © 2025 Orcinus
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

package br.com.orcinus.orca.app.module.feature.composer

import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.module.authenticationLock
import br.com.orcinus.orca.feature.composer.ComposerModule
import br.com.orcinus.orca.std.image.android.AndroidImageLoader
import br.com.orcinus.orca.std.injector.Injector
import br.com.orcinus.orca.std.injector.module.injection.lazyInjectionOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

internal class MainComposerModule(coroutineScope: CoroutineScope) : ComposerModule() {
  override val avatarLoaderDeferred = lazyInjectionOf {
    coroutineScope.async {
      Injector.from<CoreModule>().authenticationLock().scheduleUnlock {
        it.avatarLoader as AndroidImageLoader<*>
      }
    }
  }
}
