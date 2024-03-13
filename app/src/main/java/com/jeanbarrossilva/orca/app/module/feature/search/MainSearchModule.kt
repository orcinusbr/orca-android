/*
 * Copyright © 2023-2024 Orca
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

package com.jeanbarrossilva.orca.app.module.feature.search

import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.module.instanceProvider
import com.jeanbarrossilva.orca.feature.search.SearchModule
import com.jeanbarrossilva.orca.platform.navigation.Navigator
import com.jeanbarrossilva.orca.std.injector.Injector
import com.jeanbarrossilva.orca.std.injector.module.injection.injectionOf

internal class MainSearchModule(navigator: Navigator) :
  SearchModule(
    injectionOf { Injector.from<CoreModule>().instanceProvider().provide().profileSearcher },
    injectionOf { NavigatorSearchBoundary(navigator) }
  )
