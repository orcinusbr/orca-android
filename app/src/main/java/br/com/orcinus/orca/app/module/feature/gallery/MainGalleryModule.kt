/*
 * Copyright © 2023-2024 Orca
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

package br.com.orcinus.orca.app.module.feature.gallery

import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.module.instanceProvider
import br.com.orcinus.orca.feature.gallery.GalleryModule
import br.com.orcinus.orca.platform.navigation.Navigator
import br.com.orcinus.orca.std.injector.Injector
import br.com.orcinus.orca.std.injector.module.injection.injectionOf

internal class MainGalleryModule(navigator: Navigator) :
  GalleryModule(
    injectionOf { Injector.from<CoreModule>().instanceProvider().provide().postProvider },
    injectionOf { NavigatorGalleryBoundary(navigator) }
  )