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

package br.com.orcinus.orca.feature.composer

import br.com.orcinus.orca.std.func.monad.Maybe
import br.com.orcinus.orca.std.image.android.AndroidImageLoader
import br.com.orcinus.orca.std.injector.module.Inject
import br.com.orcinus.orca.std.injector.module.Module
import br.com.orcinus.orca.std.injector.module.injection.Injection
import kotlinx.coroutines.Deferred

abstract class ComposerModule : Module() {
  @Inject
  abstract val avatarLoaderDeferred: Injection<out Deferred<Maybe<*, AndroidImageLoader<*>>>>
}
