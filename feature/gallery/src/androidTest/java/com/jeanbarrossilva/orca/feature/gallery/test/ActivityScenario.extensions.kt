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
import androidx.compose.ui.res.stringResource
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.platform.app.InstrumentationRegistry
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Attachment
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.samples
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.sample
import com.jeanbarrossilva.orca.feature.gallery.test.activity.GalleryActivity
import com.jeanbarrossilva.orca.feature.gallery.ui.Gallery
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.formatted
import com.jeanbarrossilva.orca.platform.ui.core.Intent
import com.jeanbarrossilva.orca.std.imageloader.compose.Image
import com.jeanbarrossilva.orca.std.imageloader.compose.R
import com.jeanbarrossilva.orca.std.imageloader.compose.Sizing
import com.jeanbarrossilva.orca.std.imageloader.compose.rememberImageLoader

/**
 * Launches a [GalleryActivity].
 *
 * @param postID ID of the [Post] to which the [Attachment]s belong.
 * @param primaryIndex Index of the primary page.
 * @param secondary [Attachment]s to be shown in the pages other than the [primary].
 * @param primary Primary page of the [Gallery].
 */
internal fun launchGalleryActivity(
  postID: String = Post.sample.id,
  primaryIndex: Int = 0,
  secondary: List<Attachment> = Attachment.samples,
  primary: @Composable (Modifier, Sizing) -> Unit = { modifier, sizing ->
    Image(
      rememberImageLoader(R.drawable.image),
      contentDescription =
        stringResource(
          com.jeanbarrossilva.orca.feature.gallery.R.string.feature_gallery_attachment,
          1.formatted
        ),
      modifier,
      sizing
    )
  }
): ActivityScenario<GalleryActivity> {
  val context = InstrumentationRegistry.getInstrumentation().context
  val intent =
    Intent<GalleryActivity>(
      context,
      GalleryActivity.POST_KEY to postID,
      GalleryActivity.PRIMARY_INDEX_KEY to primaryIndex,
      GalleryActivity.SECONDARY_KEY to secondary
    )
  return launchActivity<GalleryActivity>(intent).onActivity { it.setPrimary(primary) }
}
