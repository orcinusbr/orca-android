/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.app.module.feature.postdetails

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Attachment
import com.jeanbarrossilva.orca.feature.gallery.GalleryActivity
import com.jeanbarrossilva.orca.feature.postdetails.PostDetailsBoundary
import com.jeanbarrossilva.orca.feature.postdetails.PostDetailsFragment
import com.jeanbarrossilva.orca.platform.ui.core.browseTo
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.std.imageloader.compose.Sizing
import java.net.URL

internal class NavigatorPostDetailsBoundary(
  private val context: Context,
  private val navigator: Navigator
) : PostDetailsBoundary {
  override fun navigateTo(url: URL) {
    context.browseTo(url)
  }

  override fun navigateToGallery(
    postID: String,
    entrypointIndex: Int,
    secondary: List<Attachment>,
    entrypoint: @Composable (Modifier, Sizing) -> Unit
  ) {
    GalleryActivity.start(context, postID, entrypointIndex, secondary, entrypoint)
  }

  override fun navigateToPostDetails(id: String) {
    PostDetailsFragment.navigate(navigator, id)
  }

  override fun pop() {
    navigator.pop()
  }
}
