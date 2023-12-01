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

package com.jeanbarrossilva.orca.platform.autos.iconography

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource

/** [ImageVector] version of this icon ID. */
val String.asImageVector
  @Composable
  @Suppress("DiscouragedApi")
  get() =
    with("icon_${replace('-', '_')}") id@{
      with(LocalContext.current) {
        try {
          ImageVector.vectorResource(
            theme,
            resources,
            resources.getIdentifier(this@id, "drawable", packageName)
          )
        } catch (_: Resources.NotFoundException) {
          throw Resources.NotFoundException(this@id)
        }
      }
    }
