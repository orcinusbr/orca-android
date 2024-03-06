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

package com.jeanbarrossilva.orca.feature.gallery.test

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import com.jeanbarrossilva.orca.composite.timeline.stat.details.formatted
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Attachment
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.Posts
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.samples
import com.jeanbarrossilva.orca.core.sample.image.CoverImageSource
import com.jeanbarrossilva.orca.feature.gallery.GalleryActivity
import com.jeanbarrossilva.orca.feature.gallery.ui.SampleGallery
import com.jeanbarrossilva.orca.platform.core.image.createSample
import com.jeanbarrossilva.orca.platform.core.withSample
import com.jeanbarrossilva.orca.platform.testing.context
import com.jeanbarrossilva.orca.std.image.compose.ComposableImageLoader

/**
 * Launches a [GalleryActivity].
 *
 * @param postID ID of the [Post] to which the [Attachment]s belong.
 * @param entrypointIndex Index at which the [entrypoint] is.
 * @param secondary [Attachment]s to be shown in the pages other than the [entrypoint].
 * @param entrypoint Entrypoint page of the [SampleGallery].
 */
internal fun launchGalleryActivity(
  postID: String = Posts.withSample.single().id,
  entrypointIndex: Int = 0,
  secondary: List<Attachment> = Attachment.samples,
  entrypoint: @Composable (ContentScale, Modifier) -> Unit = { contentScale, modifier ->
    ComposableImageLoader.createSample(CoverImageSource.Default).load()(
      stringResource(
        com.jeanbarrossilva.orca.feature.gallery.R.string.feature_gallery_attachment,
        1.formatted
      ),
      RectangleShape,
      contentScale,
      modifier
    )
  }
): ActivityScenario<GalleryActivity> {
  val intent = GalleryActivity.getIntent(context, postID, entrypointIndex, secondary)
  return launchActivity<GalleryActivity>(intent).onActivity { it.setEntrypoint(entrypoint) }
}
