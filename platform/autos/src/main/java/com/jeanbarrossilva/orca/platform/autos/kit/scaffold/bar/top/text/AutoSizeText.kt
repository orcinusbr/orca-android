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

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.text

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.text.size.AutoSizeRange
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.text.size.AutoSizer
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.text.size.rememberAutoSizeRange
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

/**
 * [Text] that is sized automatically based on the specified [range].
 *
 * @param text [String] to be displayed.
 * @param modifier [Modifier] to be applied to the underlying [Text].
 * @param style [TextStyle] with which it will be styled.
 * @param range [AutoSizeRange] within which the font size should be.
 */
@Composable
fun AutoSizeText(
  text: String,
  modifier: Modifier = Modifier,
  style: TextStyle = LocalTextStyle.current,
  range: AutoSizeRange = rememberAutoSizeRange(style.fontSize)
) {
  val density = LocalDensity.current
  var canBeDrawn by remember { mutableStateOf(false) }
  val sizer = remember(density, range) { AutoSizer(density, range) }

  Text(
    text,
    modifier.drawWithContent {
      if (canBeDrawn) {
        drawContent()
      }
    },
    fontSize = sizer.size,
    onTextLayout = { sizer.autoSize(it, canBeDrawn) { canBeDrawn = true } },
    maxLines = 1,
    style = style
  )
}

/** Preview of an [AutoSizeText]. */
@Composable
@MultiThemePreview
private fun AutoSizeTextPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) { AutoSizeText("Text") }
  }
}
