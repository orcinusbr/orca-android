/*
 * Copyright © 2024 Orcinus
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

package com.jeanbarrossilva.orca.platform.core

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.sample.auth.actor.createSample
import com.jeanbarrossilva.orca.core.sample.image.AuthorImageSource
import com.jeanbarrossilva.orca.platform.core.image.SampleComposableImageLoader
import com.jeanbarrossilva.orca.std.image.compose.ComposableImageLoader

/** [Actor.Authenticated] returned by [Actor.Authenticated.Companion.sample]. */
internal val testAuthenticatedActorSample =
  Actor.Authenticated.createSample(
    SampleComposableImageLoader.Provider.provide(AuthorImageSource.Default)
  )

/**
 * Sample [authenticated][Actor.Authenticated] [Actor] whose avatar is loaded by a
 * [ComposableImageLoader].
 */
val Actor.Authenticated.Companion.sample
  get() = testAuthenticatedActorSample
