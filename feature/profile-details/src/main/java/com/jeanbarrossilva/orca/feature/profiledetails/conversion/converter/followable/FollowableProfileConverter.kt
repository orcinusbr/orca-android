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

package com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter.followable

import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.orca.feature.profiledetails.conversion.ProfileConverter
import com.jeanbarrossilva.orca.platform.ui.core.style.toAnnotatedString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * [ProfileConverter] that converts a [FollowableProfile].
 *
 * @param coroutineScope [CoroutineScope] through which converted [Profile]'s [Follow] status toggle
 *   will be performed.
 */
internal class FollowableProfileConverter(
  private val coroutineScope: CoroutineScope,
  override val next: ProfileConverter?
) : ProfileConverter() {
  override fun onConvert(profile: Profile, colors: Colors): ProfileDetails? {
    return if (profile is FollowableProfile<*>) {
      ProfileDetails.Followable(
        profile.id,
        profile.avatarLoader,
        profile.name,
        profile.account,
        profile.bio.toAnnotatedString(colors),
        profile.url,
        profile.follow.toStatus()
      ) {
        coroutineScope.launch { profile.toggleFollow() }
      }
    } else {
      null
    }
  }
}
