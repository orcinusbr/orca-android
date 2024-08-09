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

import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import br.com.orcinus.orca.composite.timeline.stat.details.formatted
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Attachment
import br.com.orcinus.orca.core.sample.feed.profile.post.SamplePostProvider
import br.com.orcinus.orca.core.sample.feed.profile.post.content.samples
import br.com.orcinus.orca.core.sample.image.CoverImageSource
import br.com.orcinus.orca.feature.gallery.GalleryActivity
import br.com.orcinus.orca.feature.gallery.R
import br.com.orcinus.orca.platform.core.image.createSample
import br.com.orcinus.orca.platform.testing.context
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader

/**
 * Launches a [GalleryActivity].
 *
 * @param postProvider [SamplePostProvider] by which the [Post] to which the [Attachment]s to be
 *   shown belong.
 */
fun launchGalleryActivity(postProvider: SamplePostProvider): ActivityScenario<GalleryActivity> {
  val postID = postProvider.provideOneCurrent().id
  val intent =
    GalleryActivity.getIntent(context, postID, entrypointIndex = 0, secondary = Attachment.samples)
  return launchActivity<GalleryActivity>(intent).onActivity {
    it.setEntrypoint { contentScale, modifier ->
      ComposableImageLoader.createSample(CoverImageSource.Default).load()(
        stringResource(R.string.feature_gallery_attachment, 1.formatted),
        RectangleShape,
        contentScale,
        modifier
      )
    }
  }
}
