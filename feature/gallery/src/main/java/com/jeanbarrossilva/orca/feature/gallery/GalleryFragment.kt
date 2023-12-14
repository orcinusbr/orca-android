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

package com.jeanbarrossilva.orca.feature.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Attachment
import com.jeanbarrossilva.orca.feature.gallery.ui.Gallery
import com.jeanbarrossilva.orca.platform.ui.core.application
import com.jeanbarrossilva.orca.platform.ui.core.argument
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableFragment
import com.jeanbarrossilva.orca.std.imageloader.compose.Sizing
import com.jeanbarrossilva.orca.std.injector.Injector

class GalleryFragment internal constructor() : ComposableFragment() {
  private val module by lazy { Injector.from<GalleryModule>() }
  private val postID by argument<String>(POST_ID_KEY)
  private val entrypointIndex by argument<Int>(ENTRYPOINT_INDEX_KEY)
  private val secondary by argument<List<Attachment>>(SECONDARY_KEY)
  private var entrypoint by mutableStateOf<(@Composable (Modifier, Sizing) -> Unit)?>(null)
  private val viewModel by
    viewModels<GalleryViewModel> {
      GalleryViewModel.createFactory(application, module.postProvider(), postID)
    }

  internal constructor(postID: String, entrypointIndex: Int, secondary: List<Attachment>) : this() {
    arguments =
      bundleOf(
        POST_ID_KEY to postID,
        ENTRYPOINT_INDEX_KEY to entrypointIndex,
        SECONDARY_KEY to secondary
      )
  }

  @Composable
  override fun Content() {
    Gallery(viewModel, module.boundary(), entrypointIndex, secondary) { modifier, sizing ->
      entrypoint?.invoke(modifier, sizing)
    }
  }

  internal fun setEntrypoint(entrypoint: @Composable (Modifier, Sizing) -> Unit) {
    this.entrypoint = entrypoint
  }

  companion object {
    internal const val POST_ID_KEY = "post"
    internal const val ENTRYPOINT_INDEX_KEY = "entrypoint-index"
    internal const val SECONDARY_KEY = "secondary"
  }
}
