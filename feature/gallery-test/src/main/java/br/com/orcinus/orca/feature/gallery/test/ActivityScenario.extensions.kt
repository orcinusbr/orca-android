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

package br.com.orcinus.orca.feature.gallery.test

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import br.com.orcinus.orca.composite.timeline.stat.details.formatted
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Attachment
import br.com.orcinus.orca.core.sample.feed.profile.post.Posts
import br.com.orcinus.orca.core.sample.feed.profile.post.content.samples
import br.com.orcinus.orca.core.sample.image.CoverImageSource
import br.com.orcinus.orca.feature.gallery.GalleryActivity
import br.com.orcinus.orca.feature.gallery.R
import br.com.orcinus.orca.platform.core.image.createSample
import br.com.orcinus.orca.platform.core.withSample
import br.com.orcinus.orca.platform.testing.context
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader

/**
 * Launches a [GalleryActivity].
 *
 * @param postID ID of the [Post] to which the [Attachment]s belong.
 * @param entrypointIndex Index at which the [entrypoint] is.
 * @param secondary [Attachment]s to be shown in the pages other than the [entrypoint].
 * @param entrypoint Entrypoint page of the [SampleGallery].
 */
fun launchGalleryActivity(
  postID: String = Posts.withSample.single().id,
  entrypointIndex: Int = 0,
  secondary: List<Attachment> = Attachment.samples,
  entrypoint: @Composable (ContentScale, Modifier) -> Unit = { contentScale, modifier ->
    ComposableImageLoader.createSample(CoverImageSource.Default).load()(
      stringResource(R.string.feature_gallery_attachment, 1.formatted),
      RectangleShape,
      contentScale,
      modifier
    )
  }
): ActivityScenario<GalleryActivity> {
  val intent = GalleryActivity.getIntent(context, postID, entrypointIndex, secondary)
  return launchActivity<GalleryActivity>(intent).onActivity { it.setEntrypoint(entrypoint) }
}
