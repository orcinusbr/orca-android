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

package br.com.orcinus.orca.app.demo.module.core

import androidx.annotation.VisibleForTesting
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.sample.feed.profile.post.content.SampleTermMuter
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.core.sample.instance.SampleInstanceProvider
import br.com.orcinus.orca.std.injector.module.injection.lazyInjectionOf

internal class DemoCoreModule(@get:VisibleForTesting val instance: SampleInstance) :
  CoreModule(
    lazyInjectionOf { SampleInstanceProvider { instance } },
    lazyInjectionOf { instance.authenticationLock },
    lazyInjectionOf { SampleTermMuter() }
  )
