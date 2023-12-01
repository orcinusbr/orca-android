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

package com.jeanbarrossilva.orca.feature.postdetails.ui.header

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.kit.action.Hoverable
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

internal object StatDefaults {
  val contentColor
    @Composable get() = AutosTheme.colors.secondary.asColor
}

@Composable
internal fun Stat(
  modifier: Modifier = Modifier,
  contentColor: Color = StatDefaults.contentColor,
  content: @Composable RowScope.() -> Unit
) {
  Hoverable(modifier) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(AutosTheme.spacings.small.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalTextStyle provides AutosTheme.typography.bodySmall.copy(color = contentColor)
      ) {
        content()
      }
    }
  }
}

@Composable
@MultiThemePreview
private fun StatPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) {
      Stat {
        Icon(AutosTheme.iconography.comment.outlined.asImageVector, contentDescription = "Comments")
        Text("8")
      }
    }
  }
}
