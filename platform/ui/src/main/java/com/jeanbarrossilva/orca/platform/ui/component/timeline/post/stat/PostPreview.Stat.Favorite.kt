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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIconInteractiveness
import com.jeanbarrossilva.orca.platform.ui.component.stat.favorite.FavoriteStatIcon
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.PostPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.Stat
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.StatDefaults
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.StatPosition

/** Tag that identifies a [PostPreview]'s favorite count stat for testing purposes. */
const val POST_PREVIEW_FAVORITE_STAT_TAG = "post-preview-favorites-stat"

@Composable
internal fun FavoriteStat(
  position: StatPosition,
  preview: PostPreview,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isActive = remember(preview) { preview.isFavorite }

  Stat(position, onClick, modifier.testTag(POST_PREVIEW_FAVORITE_STAT_TAG)) {
    val contentColor by
      animateColorAsState(
        if (isActive) AutosTheme.colors.activation.favorite.asColor else LocalContentColor.current,
        label = "ContentColor"
      )

    FavoriteStatIcon(
      isActive,
      ActivateableStatIconInteractiveness.Interactive { onClick() },
      Modifier.size(StatDefaults.IconSize)
    )

    Text(preview.formattedFavoriteCount, color = contentColor)
  }
}

@Composable
@MultiThemePreview
private fun InactiveFavoriteStatPreview() {
  AutosTheme {
    FavoriteStat(StatPosition.SUBSEQUENT, PostPreview.sample.copy(isFavorite = false), onClick = {})
  }
}

@Composable
@MultiThemePreview
private fun ActiveFavoriteStatPreview() {
  AutosTheme {
    FavoriteStat(StatPosition.SUBSEQUENT, PostPreview.sample.copy(isFavorite = true), onClick = {})
  }
}
