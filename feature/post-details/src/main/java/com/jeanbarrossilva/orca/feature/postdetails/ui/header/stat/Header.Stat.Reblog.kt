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

package com.jeanbarrossilva.orca.feature.postdetails.ui.header.stat

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.feature.postdetails.PostDetails
import com.jeanbarrossilva.orca.feature.postdetails.ui.header.Stat
import com.jeanbarrossilva.orca.feature.postdetails.ui.header.StatDefaults
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIconInteractiveness
import com.jeanbarrossilva.orca.platform.ui.component.stat.reblog.ReblogStatIcon

@Composable
internal fun ReblogStat(details: PostDetails, onClick: () -> Unit, modifier: Modifier = Modifier) {
  val isActive = remember(details, details::isReblogged)
  val contentColor by
    animateColorAsState(
      if (isActive) AutosTheme.colors.activation.reposted.asColor else StatDefaults.contentColor,
      label = "ContentColor"
    )

  Stat(contentColor = contentColor) {
    ReblogStatIcon(
      isActive,
      ActivateableStatIconInteractiveness.Interactive { onClick() },
      modifier.size(24.dp)
    )

    Text(details.formattedReblogCount)
  }
}

@Composable
@MultiThemePreview
private fun InactiveReblogStatPreview() {
  AutosTheme { ReblogStat(PostDetails.sample.copy(isReblogged = false), onClick = {}) }
}

@Composable
@MultiThemePreview
private fun ActiveReblogStatPreview() {
  AutosTheme { ReblogStat(PostDetails.sample.copy(isReblogged = true), onClick = {}) }
}
