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

package com.jeanbarrossilva.orca.app.demo.module.core

import android.content.Context
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.SampleTermMuter
import com.jeanbarrossilva.orca.core.sample.instance.createSample
import com.jeanbarrossilva.orca.platform.ui.component.avatar.createSample
import com.jeanbarrossilva.orca.std.image.ImageLoader
import com.jeanbarrossilva.orca.std.injector.Injector

internal object DemoCoreModule :
  CoreModule(
    { DemoCoreModule.instanceProvider },
    { DemoCoreModule.instanceProvider.provide().authenticationLock },
    { SampleTermMuter() }
  ) {
  private val instanceProvider by lazy { InstanceProvider.createSample(imageLoaderProvider) }

  private val imageLoaderProvider
    get() = ImageLoader.Provider.createSample(context)

  private val context
    get() = Injector.get<Context>()
}
