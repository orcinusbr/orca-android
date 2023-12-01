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

package com.jeanbarrossilva.orca.platform.ui.component.timeline.post.stat

import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIconInteractiveness
import com.jeanbarrossilva.orca.platform.ui.component.stat.reblog.ReblogStatIcon
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.POST_PREVIEW_REBLOG_COUNT_STAT_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.PostPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.Stat
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.StatPosition

@Composable
internal fun ReblogStat(
  position: StatPosition,
  preview: PostPreview,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isActive = remember(preview, preview::isReblogged)

  Stat(position, onClick, modifier.testTag(POST_PREVIEW_REBLOG_COUNT_STAT_TAG)) {
    val contentColor by
      animateColorAsState(
        if (isActive) Color(0xFF81C784) else LocalContentColor.current,
        label = "ContentColor"
      )

    ReblogStatIcon(isActive, ActivateableStatIconInteractiveness.Interactive { onClick() })

    Text(preview.formattedReblogCount, color = contentColor)
  }
}

@Composable
@MultiThemePreview
private fun InactiveReblogStatPreview() {
  AutosTheme {
    ReblogStat(StatPosition.SUBSEQUENT, PostPreview.sample.copy(isReblogged = false), onClick = {})
  }
}

@Composable
@MultiThemePreview
private fun ActiveReblogStatPreview() {
  AutosTheme {
    ReblogStat(StatPosition.SUBSEQUENT, PostPreview.sample.copy(isReblogged = true), onClick = {})
  }
}
