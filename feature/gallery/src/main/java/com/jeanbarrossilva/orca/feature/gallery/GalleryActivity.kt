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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.orca.composite.composable.ComposableActivity
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Attachment
import com.jeanbarrossilva.orca.feature.gallery.ui.Gallery
import com.jeanbarrossilva.orca.platform.ui.core.Intent
import com.jeanbarrossilva.orca.platform.ui.core.extra
import com.jeanbarrossilva.orca.std.injector.Injector
import com.jeanbarrossilva.platform.starter.on

class GalleryActivity internal constructor() : ComposableActivity() {
  private val module by lazy { Injector.from<GalleryModule>() }
  private val postID by extra<String>(POST_ID_KEY)
  private val entrypointIndex by extra<Int>(ENTRYPOINT_INDEX_KEY)
  private val secondary by extra<List<Attachment>>(SECONDARY_KEY)

  @set:JvmName("_setEntrypoint")
  private var entrypoint by mutableStateOf<(@Composable (Modifier) -> Unit)?>(null)

  private val viewModel by
    viewModels<GalleryViewModel> {
      GalleryViewModel.createFactory(application, module.postProvider(), postID)
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
  }

  @Composable
  override fun Content() {
    Gallery(viewModel, module.boundary(), entrypointIndex, secondary, onClose = ::finish) {
      entrypoint?.invoke(it)
    }
  }

  fun setEntrypoint(entrypoint: @Composable (Modifier) -> Unit) {
    this.entrypoint = entrypoint
  }

  companion object {
    private const val POST_ID_KEY = "post"
    private const val ENTRYPOINT_INDEX_KEY = "entrypoint-index"
    private const val SECONDARY_KEY = "secondary"

    fun getIntent(
      context: Context,
      postID: String,
      entrypointIndex: Int,
      secondary: List<Attachment>
    ): Intent {
      return Intent<GalleryActivity>(
        context,
        POST_ID_KEY to postID,
        ENTRYPOINT_INDEX_KEY to entrypointIndex,
        SECONDARY_KEY to secondary
      )
    }

    fun start(
      context: Context,
      postID: String,
      entrypointIndex: Int,
      secondary: List<Attachment>,
      entrypoint: @Composable (Modifier) -> Unit
    ) {
      context
        .on<GalleryActivity>()
        .with(
          POST_ID_KEY to postID,
          ENTRYPOINT_INDEX_KEY to entrypointIndex,
          SECONDARY_KEY to secondary
        )
        .start { it.setEntrypoint(entrypoint) }
    }
  }
}
