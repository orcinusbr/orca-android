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

package com.jeanbarrossilva.orca.app.module.feature.search

import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetailsFragment
import com.jeanbarrossilva.orca.feature.search.SearchBoundary
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator

internal class NavigatorSearchBoundary(private val navigator: Navigator) : SearchBoundary {
  override fun navigateToProfileDetails(id: String) {
    ProfileDetailsFragment.navigate(navigator, id)
  }

  override fun pop() {
    navigator.pop()
  }
}
