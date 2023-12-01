/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.feature.profiledetails.test

import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.feed.profile.type.followable.createSample
import com.jeanbarrossilva.orca.core.sample.test.image.TestSampleImageLoader
import com.jeanbarrossilva.orca.core.sample.test.instance.sample
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter.followable.toStatus
import com.jeanbarrossilva.orca.platform.ui.core.style.toAnnotatedString

/**
 * Creates a sample [ProfileDetails.Followable].
 *
 * @param onStatusToggle Operation to be performed whenever the [ProfileDetails.Followable]'s
 *   [status][ProfileDetails.Followable.status] is changed.
 */
internal fun ProfileDetails.Followable.Companion.createSample(
  onStatusToggle: () -> Unit
): ProfileDetails.Followable {
  val profile =
    FollowableProfile.createSample(
      Instance.sample.profileWriter,
      Instance.sample.postProvider,
      Follow.Public.following(),
      TestSampleImageLoader.Provider
    )
  return ProfileDetails.Followable(
    profile.id,
    profile.avatarLoader,
    profile.name,
    profile.account,
    profile.bio.toAnnotatedString(Colors.LIGHT),
    profile.url,
    profile.follow.toStatus(),
    onStatusToggle
  )
}
