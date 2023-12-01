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

package com.jeanbarrossilva.orca.feature.postdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.core.style.toAnnotatedString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Converts this [Post] into a [Flow] of [PostDetails].
 *
 * @param colors [Colors] by which the emitted [PostDetails]' [PostDetails.text] can be colored.
 */
internal fun Post.toPostDetailsFlow(colors: Colors): Flow<PostDetails> {
  return combine(
    comment.countFlow,
    favorite.isEnabledFlow,
    favorite.countFlow,
    repost.isEnabledFlow,
    repost.countFlow
  ) { commentCount, isFavorite, favoriteCount, isReblogged, reblogCount ->
    PostDetails(
      id,
      author.avatarLoader,
      author.name,
      author.account,
      content.text.toAnnotatedString(colors),
      content.highlight,
      publicationDateTime,
      commentCount,
      isFavorite,
      favoriteCount,
      isReblogged,
      reblogCount,
      url
    )
  }
}

/** Converts this [Post] into [PostDetails]. */
@Composable
internal fun Post.toPostDetails(): PostDetails {
  val commentCount by comment.countFlow.collectAsState()
  val isFavorite by favorite.isEnabledFlow.collectAsState()
  val favoriteCount by favorite.countFlow.collectAsState()
  val isReblogged by repost.isEnabledFlow.collectAsState()
  val reblogCount by repost.countFlow.collectAsState()
  return PostDetails(
    id,
    author.avatarLoader,
    author.name,
    author.account,
    content.text.toAnnotatedString(AutosTheme.colors),
    content.highlight,
    publicationDateTime,
    commentCount,
    isFavorite,
    favoriteCount,
    isReblogged,
    reblogCount,
    url
  )
}
