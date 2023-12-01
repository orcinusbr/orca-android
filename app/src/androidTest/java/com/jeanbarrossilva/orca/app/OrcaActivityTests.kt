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

package com.jeanbarrossilva.orca.app

import androidx.test.core.app.launchActivity
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsRule
import com.jeanbarrossilva.orca.core.mastodon.auth.authorization.MastodonAuthorizationActivity
import org.junit.Rule
import org.junit.Test

internal class OrcaActivityTests {
  @get:Rule val intentsRule = IntentsRule()

  @Test
  fun navigatesToAuthorization() {
    launchActivity<OrcaActivity>().use {
      intended(hasComponent(MastodonAuthorizationActivity::class.qualifiedName))
    }
  }
}
