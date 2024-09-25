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

package br.com.orcinus.orca.core.sample.feed.profile

import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.ProfileProvider
import br.com.orcinus.orca.core.sample.feed.profile.composition.Composer
import br.com.orcinus.orca.core.sample.feed.profile.type.editable.SampleEditableProfile
import br.com.orcinus.orca.core.sample.feed.profile.type.editable.replacingOnceBy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking

/** [ProfileProvider] that provides sample [Profile]s. */
class SampleProfileProvider : ProfileProvider() {
  /** [MutableStateFlow] that provides the [Composer]s. */
  @PublishedApi internal val composersFlow = MutableStateFlow(emptyList<Composer>())

  public override suspend fun contains(id: String): Boolean {
    val ids = provideCurrent().map(Composer::id)
    return id in ids
  }

  public override fun createNonexistentProfileException(): NonexistentProfileException {
    return NonexistentProfileException(cause = null)
  }

  override suspend fun onProvide(id: String): Flow<Composer> {
    return composersFlow.mapNotNull { composers ->
      composers.find { composer -> composer.id == id }
    }
  }

  /** Provides one [Composer] out of the current ones. */
  fun provideOneCurrent(): Composer {
    return provideCurrent<SampleEditableProfile>()
  }

  /**
   * Provides a [Composer] of the specified type.
   *
   * @param T [Composer] to be obtained.
   * @throws NoSuchElementException If no [Composer] whose type is the specified one is found.
   */
  @Throws(NoSuchElementException::class)
  inline fun <reified T : Composer> provideCurrent(): T {
    return provideCurrent().find { it is T } as? T
      ?: throw NoSuchElementException("${T::class.qualifiedName}")
  }

  /** Provides the [Composer]s currently added. */
  fun provideCurrent(): List<Composer> {
    return composersFlow.value
  }

  /**
   * Provides the current [Composer] identified as [id].
   *
   * @param id ID of the [Composer] to be provided.
   * @see Composer.id
   */
  @PublishedApi
  internal fun provideCurrent(id: String): Composer {
    /*
     * Profiles emitted to SampleProfileProvider's onProvide(String): Flow<Composer>'s flow are
     * obtained through an in-memory, non-blocking lookup.
     */
    return runBlocking { provide(id).first() as Composer }
  }

  /**
   * Adds various [Composer]s.
   *
   * @param composers [Composer]s to be added.
   */
  internal fun add(vararg composers: Composer) {
    composersFlow.value += composers
  }

  /**
   * Replaces the currently existing [Composer] identified as [id] by its updated version.
   *
   * @param id ID of the [Composer] to be updated.
   * @param update Changes to be made to the existing [Composer].
   * @throws ProfileProvider.NonexistentProfileException If no [Composer] with such an ID exists.
   * @see Composer.id
   */
  internal suspend fun update(id: String, update: Composer.() -> Composer) {
    if (contains(id)) {
      composersFlow.update { profiles ->
        profiles.replacingOnceBy(update) { profile -> profile.id == id }
      }
    } else {
      throw createNonexistentProfileException()
    }
  }
}
