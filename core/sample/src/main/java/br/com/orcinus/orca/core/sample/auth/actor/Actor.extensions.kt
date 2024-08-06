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

package br.com.orcinus.orca.core.sample.auth.actor

import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoader
import br.com.orcinus.orca.std.image.test.NoOpImageLoader

/**
 * [Authenticated][Actor.Authenticated] [Actor] returned by [Actor.Authenticated.Companion.sample].
 */
private val testSampleAuthenticatedActor = Actor.Authenticated.createSample(NoOpImageLoader)

/** [Authenticated][Actor.Authenticated] [Actor] whose avatar is loaded by a [NoOpImageLoader]. */
val Actor.Authenticated.Companion.sample
  get() = testSampleAuthenticatedActor

/**
 * Creates a sample [authenticated][Actor.Authenticated] [Actor].
 *
 * @param avatarLoader [ImageLoader] by which the avatar will be loaded.
 */
fun Actor.Authenticated.Companion.createSample(avatarLoader: SomeImageLoader): Actor.Authenticated {
  return Actor.Authenticated("sample-id", "sample-access-token", avatarLoader)
}
