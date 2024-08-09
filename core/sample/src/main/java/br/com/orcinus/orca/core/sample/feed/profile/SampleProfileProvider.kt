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
import br.com.orcinus.orca.core.feed.profile.type.followable.Follow
import br.com.orcinus.orca.core.sample.feed.profile.type.editable.replacingOnceBy
import br.com.orcinus.orca.core.sample.feed.profile.type.followable.SampleFollowableProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking

/** [ProfileProvider] that provides sample [Profile]s. */
class SampleProfileProvider : ProfileProvider() {
  /** [MutableStateFlow] that provides the [Profile]s. */
  @PublishedApi internal val profilesFlow = MutableStateFlow(emptyList<Profile>())

  public override suspend fun contains(id: String): Boolean {
    val ids = provideCurrent().map(Profile::id)
    return id in ids
  }

  public override fun createNonexistentProfileException(): NonexistentProfileException {
    return NonexistentProfileException(cause = null)
  }

  override suspend fun onProvide(id: String): Flow<Profile> {
    return profilesFlow.mapNotNull { profiles -> profiles.find { profile -> profile.id == id } }
  }

  /**
   * Provides a [Profile] of the specified type.
   *
   * @param T [Profile] to be obtained.
   * @throws NoSuchElementException If no [Profile] whose type is the specified one is found.
   */
  @Throws(NoSuchElementException::class)
  inline fun <reified T : Profile> provideCurrent(): T {
    return provideCurrent().find { it is T } as? T
      ?: throw NoSuchElementException("${T::class.qualifiedName}")
  }

  /** Provides the [Profile]s currently added. */
  fun provideCurrent(): List<Profile> {
    return profilesFlow.value
  }

  /**
   * Provides the current [Profile] identified as [id].
   *
   * @param id ID of the [Profile] to be provided.
   * @see Profile.id
   */
  @PublishedApi
  internal fun provideCurrent(id: String): Profile {
    /*
     * Profiles emitted to SampleProfileProvider's onProvide(String): Flow<Profile>'s flow are
     * obtained through an in-memory, non-blocking lookup.
     */
    return runBlocking { provide(id).first() }
  }

  /**
   * Adds various [Profile]s.
   *
   * @param profiles [Profile]s to be added.
   */
  internal fun add(vararg profiles: Profile) {
    profilesFlow.value += profiles
  }

  /**
   * Updates the follow status of the [Profile] whose ID is equal the given [id].
   *
   * @param id The [Profile]'s ID.
   * @param follow [Follow] status to update the follow status to.
   * @throws ProfileProvider.NonexistentProfileException If no [Profile] with such an ID exists.
   * @see SampleFollowableProfile.follow
   * @see SampleFollowableProfile.id
   */
  @Throws(NonexistentProfileException::class)
  internal suspend fun <T : Follow> updateFollow(id: String, follow: T) {
    update(id) {
      @Suppress("UNCHECKED_CAST") (this as SampleFollowableProfile<T>).copy(follow = follow)
    }
  }

  /**
   * Replaces the currently existing [Profile] identified as [id] by its updated version.
   *
   * @param id ID of the [Profile] to be updated.
   * @param update Changes to be made to the existing [Profile].
   * @throws ProfileProvider.NonexistentProfileException If no [Profile] with such an ID exists.
   * @see Profile.id
   */
  internal suspend fun update(id: String, update: Profile.() -> Profile) {
    if (contains(id)) {
      profilesFlow.update { profiles ->
        profiles.replacingOnceBy(update) { profile -> profile.id == id }
      }
    } else {
      throw createNonexistentProfileException()
    }
  }
}
