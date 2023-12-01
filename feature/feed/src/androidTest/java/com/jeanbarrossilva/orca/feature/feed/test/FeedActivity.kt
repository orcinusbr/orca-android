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

package com.jeanbarrossilva.orca.feature.feed.test

import android.content.Intent
import androidx.navigation.NavGraphBuilder
import androidx.test.platform.app.InstrumentationRegistry
import com.jeanbarrossilva.orca.feature.feed.FeedFragment
import com.jeanbarrossilva.orca.platform.ui.core.Intent
import com.jeanbarrossilva.orca.platform.ui.test.core.SingleFragmentActivity

internal class FeedActivity : SingleFragmentActivity() {
  override val route = "feed"

  override fun NavGraphBuilder.add() {
    fragment<FeedFragment>()
  }

  companion object {
    fun getIntent(userID: String): Intent {
      val context = InstrumentationRegistry.getInstrumentation().context
      return Intent<FeedActivity>(context, FeedFragment.USER_ID_KEY to userID)
    }
  }
}
