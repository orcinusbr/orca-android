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

package com.jeanbarrossilva.orca.feature.profiledetails.conversion

import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.sample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.type.sample
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.orca.feature.profiledetails.test.createSample
import com.jeanbarrossilva.orca.feature.profiledetails.test.sample
import kotlinx.coroutines.test.TestScope
import org.junit.Assert.assertEquals
import org.junit.Test

internal class ProfileConverterFactoryTests {
  private val coroutineScope = TestScope()

  @Test
  fun createdConverterConvertsDefaultProfile() {
    assertEquals(
      ProfileDetails.Default.sample,
      ProfileConverterFactory.create(coroutineScope).convert(Profile.sample, Colors.LIGHT)
    )
  }

  @Test
  fun createdConverterConvertsEditableProfile() {
    assertEquals(
      ProfileDetails.Editable.sample,
      ProfileConverterFactory.create(coroutineScope).convert(EditableProfile.sample, Colors.LIGHT)
    )
  }

  @Test
  fun createdConverterConvertsFollowableProfile() {
    val onStatusToggle = {}
    assertEquals(
      ProfileDetails.Followable.createSample(onStatusToggle),
      ProfileConverterFactory.create(coroutineScope)
        .convert(FollowableProfile.sample, Colors.LIGHT)
        .let { it as ProfileDetails.Followable }
        .copy(onStatusToggle = onStatusToggle)
    )
  }
}
