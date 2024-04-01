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

package br.com.orcinus.orca.app.module.feature.feed

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import br.com.orcinus.orca.core.feed.profile.post.content.Attachment
import br.com.orcinus.orca.ext.intents.browseTo
import br.com.orcinus.orca.feature.composer.ComposerActivity
import br.com.orcinus.orca.feature.feed.FeedBoundary
import br.com.orcinus.orca.feature.gallery.GalleryActivity
import br.com.orcinus.orca.feature.postdetails.PostDetailsFragment
import br.com.orcinus.orca.feature.search.SearchActivity
import br.com.orcinus.orca.platform.navigation.Navigator
import java.net.URL

internal class NavigatorFeedBoundary(
  private val context: Context,
  private val navigator: Navigator
) : FeedBoundary {
  override fun navigateToSearch() {
    SearchActivity.start(context)
  }

  override fun navigateTo(url: URL) {
    context.browseTo(url)
  }

  override fun navigateToGallery(
    postID: String,
    entrypointIndex: Int,
    secondary: List<Attachment>,
    entrypoint: @Composable (ContentScale, Modifier) -> Unit
  ) {
    GalleryActivity.start(context, postID, entrypointIndex, secondary, entrypoint)
  }

  override fun navigateToPostDetails(id: String) {
    PostDetailsFragment.navigate(navigator, id)
  }

  override fun navigateToComposer() {
    ComposerActivity.start(context)
  }
}
