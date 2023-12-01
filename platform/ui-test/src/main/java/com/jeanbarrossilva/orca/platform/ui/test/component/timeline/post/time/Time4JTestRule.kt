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

package com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.time

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import net.time4j.android.ApplicationStarter
import org.junit.rules.ExternalResource

/**
 * [ExternalResource] that initializes the Time4J library before each test.
 *
 * @see ApplicationStarter.initialize
 */
class Time4JTestRule : ExternalResource() {
  override fun before() {
    val application = ApplicationProvider.getApplicationContext<Application>()
    ApplicationStarter.initialize(application)
  }
}
