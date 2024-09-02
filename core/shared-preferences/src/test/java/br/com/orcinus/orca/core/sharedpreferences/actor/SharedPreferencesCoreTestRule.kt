/*
 * Copyright © 2023-2024 Orcinus
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

package br.com.orcinus.orca.core.sharedpreferences.actor

import br.com.orcinus.orca.core.sharedpreferences.actor.mirror.image.NoOpImageLoaderProviderFactory
import br.com.orcinus.orca.core.sharedpreferences.feed.profile.post.content.SharedPreferencesTermMuter
import br.com.orcinus.orca.platform.testing.context
import org.junit.rules.ExternalResource

internal class SharedPreferencesCoreTestRule : ExternalResource() {
  lateinit var actorProvider: SharedPreferencesActorProvider
    private set

  lateinit var termMuter: SharedPreferencesTermMuter
    private set

  override fun before() {
    actorProvider = SharedPreferencesActorProvider(context, NoOpImageLoaderProviderFactory)
    termMuter = SharedPreferencesTermMuter(context)
  }

  override fun after() {
    actorProvider.reset()
    termMuter.reset()
  }
}
