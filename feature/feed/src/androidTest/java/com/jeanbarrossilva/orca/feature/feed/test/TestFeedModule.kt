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

package com.jeanbarrossilva.orca.feature.feed.test

import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.feature.feed.FeedModule
import com.jeanbarrossilva.orca.platform.autos.reactivity.OnBottomAreaAvailabilityChangeListener
import com.jeanbarrossilva.orca.platform.core.sample
import com.jeanbarrossilva.orca.std.injector.module.injection.injectionOf

internal object TestFeedModule :
  FeedModule(
    injectionOf { Instance.sample.feedProvider },
    injectionOf { Instance.sample.postProvider },
    injectionOf { TestFeedBoundary() },
    injectionOf { OnBottomAreaAvailabilityChangeListener.empty }
  )
