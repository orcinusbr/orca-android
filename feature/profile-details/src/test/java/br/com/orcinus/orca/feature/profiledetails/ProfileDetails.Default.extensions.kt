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

package br.com.orcinus.orca.feature.profiledetails

import br.com.orcinus.orca.autos.colors.Colors
import br.com.orcinus.orca.composite.timeline.text.annotated.toAnnotatedString
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.type.editable.EditableProfile
import br.com.orcinus.orca.core.sample.feed.profile.SampleProfileProvider
import kotlinx.coroutines.flow.Flow

/**
 * Creates a sample [ProfileDetails.Default].
 *
 * @param profileProvider [SampleProfileProvider] from which a [Profile] to be converted into the
 *   created [ProfileDetails.Default] is provided.
 */
internal fun ProfileDetails.Default.Companion.createSample(
  profileProvider: SampleProfileProvider,
  delegateProfile: Profile = getSampleDelegateProfile(profileProvider)
): ProfileDetails.Default {
  return ProfileDetails.Default(
    delegateProfile.id,
    delegateProfile.avatarLoader,
    delegateProfile.name,
    delegateProfile.account,
    delegateProfile.bio.toAnnotatedString(Colors.LIGHT),
    delegateProfile.uri
  )
}

/**
 * Obtains the [Profile] based on which a sample default [ProfileDetails] is created.
 *
 * @param profileProvider [SampleProfileProvider] by which the [Profile] will be provided.
 * @see ProfileDetails.Default.Companion.createSample
 */
internal fun ProfileDetails.Default.Companion.getSampleDelegateProfile(
  profileProvider: SampleProfileProvider
): Profile {
  return object : Profile {
    /**
     * [Profile] to which this one's characteristics (expect for its [id]) and behavior is
     * delegated.
     */
    private val delegate = profileProvider.provideCurrent<EditableProfile>()

    override val id = delegate.id
    override val account = delegate.account
    override val avatarLoader = delegate.avatarLoader
    override val name = delegate.name
    override val bio = delegate.bio
    override val followerCount = delegate.followerCount
    override val followingCount = delegate.followingCount
    override val uri = delegate.uri

    override suspend fun getPosts(page: Int): Flow<List<Post>> {
      return delegate.getPosts(page)
    }
  }
}
