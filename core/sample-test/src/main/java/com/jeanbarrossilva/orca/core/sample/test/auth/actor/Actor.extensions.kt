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

package com.jeanbarrossilva.orca.core.sample.test.auth.actor

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.sample.auth.actor.createSample
import com.jeanbarrossilva.orca.core.sample.image.AuthorImageSource
import com.jeanbarrossilva.orca.core.sample.test.image.TestSampleImageLoader

/** [Actor.Authenticated] returned by [Actor.Authenticated.Companion.sample]. */
internal val testAuthenticatedActorSample =
  Actor.Authenticated.createSample(
    TestSampleImageLoader.Provider.provide(AuthorImageSource.Default)
  )

/** Test sample [authenticated][Actor.Authenticated] [Actor]. */
val Actor.Authenticated.Companion.sample
  get() = testAuthenticatedActorSample
