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

package com.jeanbarrossilva.orca.feature.gallery.test.activity

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.fragment.NavHostFragment
import com.jeanbarrossilva.orca.feature.gallery.GalleryFragment
import com.jeanbarrossilva.orca.platform.ui.test.core.SingleFragmentActivity
import com.jeanbarrossilva.orca.std.imageloader.compose.Sizing

internal class GalleryActivity : SingleFragmentActivity() {
  override val route = "gallery"

  override fun NavGraphBuilder.add() {
    fragment<GalleryFragment>()
  }

  fun setEntrypoint(entrypoint: @Composable (Modifier, Sizing) -> Unit) {
    supportFragmentManager.fragments
      .single()
      .let { it as NavHostFragment }
      .childFragmentManager
      .fragments
      .single()
      .let { it as GalleryFragment }
      .setEntrypoint(entrypoint)
  }

  companion object {
    const val POST_KEY = GalleryFragment.POST_ID_KEY
    const val ENTRYPOINT_INDEX_KEY = GalleryFragment.ENTRYPOINT_INDEX_KEY
    const val SECONDARY_KEY = GalleryFragment.SECONDARY_KEY
  }
}
