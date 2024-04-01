/*
 * Copyright © 2023-2024 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.feature.postdetails

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import br.com.orcinus.orca.core.feed.profile.post.content.Attachment
import java.net.URL

interface PostDetailsBoundary {
  fun navigateTo(url: URL)

  fun navigateToGallery(
    postID: String,
    entrypointIndex: Int,
    secondary: List<Attachment>,
    entrypoint: @Composable (ContentScale, Modifier) -> Unit
  )

  fun navigateToPostDetails(id: String)

  fun pop()
}
