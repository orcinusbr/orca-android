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

package com.jeanbarrossilva.orca.core.sample.test.instance

import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.instance.SampleInstance
import org.junit.rules.ExternalResource

/**
 * [ExternalResource] that resets the [Instance.Companion.sample]'s writers (such as
 * [SampleInstance.profileWriter] and [SampleInstance.postWriter]) at the end of every test.
 */
class SampleInstanceTestRule(private val instance: SampleInstance = Instance.sample) :
  ExternalResource() {
  override fun after() {
    Instance.sample.profileWriter.reset()
    Instance.sample.postWriter.reset()
  }
}
