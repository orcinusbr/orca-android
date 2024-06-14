/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.app.module.feature.profiledetails

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import br.com.orcinus.orca.core.feed.profile.post.content.Attachment
import br.com.orcinus.orca.ext.intents.browseTo
import br.com.orcinus.orca.feature.gallery.GalleryActivity
import br.com.orcinus.orca.feature.postdetails.PostDetailsFragment
import br.com.orcinus.orca.feature.profiledetails.ProfileDetailsBoundary
import br.com.orcinus.orca.platform.navigation.BackStack
import br.com.orcinus.orca.platform.navigation.Navigator
import java.net.URI

internal class MainProfileDetailsBoundary(private val context: Context) : ProfileDetailsBoundary {
  override fun navigateTo(uri: URI) {
    context.browseTo(uri)
  }

  override fun navigateToGallery(
    postID: String,
    entrypointIndex: Int,
    secondary: List<Attachment>,
    entrypoint: @Composable (ContentScale, Modifier) -> Unit
  ) {
    GalleryActivity.start(context, postID, entrypointIndex, secondary, entrypoint)
  }

  override fun navigateToPostDetails(navigator: Navigator, backStack: BackStack, id: String) {
    PostDetailsFragment.navigate(navigator, backStack, id)
  }
}
