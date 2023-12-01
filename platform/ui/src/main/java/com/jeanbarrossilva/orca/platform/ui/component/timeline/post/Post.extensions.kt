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

package com.jeanbarrossilva.orca.platform.ui.component.timeline.post

import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.repost.Repost
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.core.style.toAnnotatedString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Converts this [Post] into a [Flow] of [PostPreview].
 *
 * @param colors [Colors] by which the emitted [PostPreview]s' [PostPreview.text] can be colored.
 */
fun Post.toPostPreviewFlow(colors: Colors): Flow<PostPreview> {
  return combine(
    comment.countFlow,
    favorite.isEnabledFlow,
    favorite.countFlow,
    repost.isEnabledFlow,
    repost.countFlow
  ) { _, _, _, _, _ ->
    toPostPreview(colors)
  }
}

/** Converts this [Post] into a [PostPreview]. */
@Composable
internal fun Post.toPostPreview(): PostPreview {
  return toPostPreview(AutosTheme.colors)
}

/**
 * Converts this [Post] into a [PostPreview].
 *
 * @param colors [Colors] by which the resulting [PostPreview]'s [PostPreview.text] can be colored.
 */
internal fun Post.toPostPreview(colors: Colors): PostPreview {
  return PostPreview(
    id,
    author.avatarLoader,
    author.name,
    author.account,
    if (this is Repost) reposter.name else null,
    content.text.toAnnotatedString(colors),
    content.highlight,
    publicationDateTime,
    comment.count,
    favorite.isEnabled,
    favorite.count,
    repost.isEnabled,
    repost.count,
    url
  )
}
