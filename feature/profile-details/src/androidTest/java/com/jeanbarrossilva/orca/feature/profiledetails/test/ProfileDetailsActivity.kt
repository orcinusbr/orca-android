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

import android.content.Intent
import androidx.navigation.NavGraphBuilder
import androidx.test.platform.app.InstrumentationRegistry
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetailsFragment
import com.jeanbarrossilva.orca.feature.profiledetails.navigation.BackwardsNavigationState
import com.jeanbarrossilva.orca.platform.ui.core.Intent
import com.jeanbarrossilva.orca.platform.ui.test.core.SingleFragmentActivity

internal class ProfileDetailsActivity : SingleFragmentActivity() {
  override val route = "profile-details"

  override fun NavGraphBuilder.add() {
    fragment<ProfileDetailsFragment>()
  }

  companion object {
    fun getIntent(backwardsNavigationState: BackwardsNavigationState, id: String): Intent {
      val context = InstrumentationRegistry.getInstrumentation().context
      return Intent<ProfileDetailsActivity>(
        context,
        ProfileDetailsFragment.BACKWARDS_NAVIGATION_STATE_KEY to backwardsNavigationState,
        ProfileDetailsFragment.ID_KEY to id
      )
    }
  }
}
