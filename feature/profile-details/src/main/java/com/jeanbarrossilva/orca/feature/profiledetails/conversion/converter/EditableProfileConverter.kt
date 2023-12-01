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

package com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter

import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.orca.feature.profiledetails.conversion.ProfileConverter
import com.jeanbarrossilva.orca.platform.ui.core.style.toAnnotatedString

/** [ProfileConverter] that converts an [EditableProfile]. */
internal class EditableProfileConverter(override val next: ProfileConverter?) : ProfileConverter() {
  override fun onConvert(profile: Profile, colors: Colors): ProfileDetails? {
    return if (profile is EditableProfile) {
      ProfileDetails.Editable(
        profile.id,
        profile.avatarLoader,
        profile.name,
        profile.account,
        profile.bio.toAnnotatedString(colors),
        profile.url
      )
    } else {
      null
    }
  }
}
