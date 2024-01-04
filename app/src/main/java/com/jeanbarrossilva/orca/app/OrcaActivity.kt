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

package com.jeanbarrossilva.orca.app

import android.content.Context
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.jeanbarrossilva.orca.app.databinding.ActivityOrcaBinding
import com.jeanbarrossilva.orca.app.module.core.MainMastodonCoreModule
import com.jeanbarrossilva.orca.app.module.feature.feed.MainFeedModule
import com.jeanbarrossilva.orca.app.module.feature.gallery.MainGalleryModule
import com.jeanbarrossilva.orca.app.module.feature.postdetails.MainPostDetailsModule
import com.jeanbarrossilva.orca.app.module.feature.profiledetails.MainProfileDetailsModule
import com.jeanbarrossilva.orca.app.module.feature.search.MainSearchModule
import com.jeanbarrossilva.orca.app.module.feature.settings.MainSettingsModule
import com.jeanbarrossilva.orca.app.module.feature.settings.termmuting.MainTermMutingModule
import com.jeanbarrossilva.orca.app.navigation.BottomNavigation
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.feature.feed.FeedModule
import com.jeanbarrossilva.orca.feature.gallery.GalleryModule
import com.jeanbarrossilva.orca.feature.postdetails.PostDetailsModule
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetailsModule
import com.jeanbarrossilva.orca.feature.search.SearchModule
import com.jeanbarrossilva.orca.feature.settings.SettingsModule
import com.jeanbarrossilva.orca.feature.settings.termmuting.TermMutingModule
import com.jeanbarrossilva.orca.platform.autos.reactivity.OnBottomAreaAvailabilityChangeListener
import com.jeanbarrossilva.orca.platform.ui.core.navigation.NavigationActivity
import com.jeanbarrossilva.orca.std.injector.Injector
import kotlinx.coroutines.launch

open class OrcaActivity : NavigationActivity(), OnBottomAreaAvailabilityChangeListener {
  private var binding: ActivityOrcaBinding? = null
  private var constraintSet: ConstraintSet? = null

  protected open val coreModule: CoreModule = MainMastodonCoreModule

  final override val height: Int
    get() = binding?.bottomNavigationView?.height ?: 0

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    binding = ActivityOrcaBinding.inflate(layoutInflater)
    constraintSet = ConstraintSet().apply { clone(binding?.root) }
    setContentView(binding?.root)
    inject()
    navigateOnBottomNavigationItemSelection()
    navigateToDefaultDestination()
  }

  override fun onDestroy() {
    super.onDestroy()
    constraintSet = null
    binding = null
    Injector.clear()
  }

  final override fun getCurrentOffsetY(): Float {
    return constraintSet?.getConstraint(R.id.bottom_navigation_view)?.transform?.translationY ?: 0f
  }

  final override fun onBottomAreaAvailabilityChange(offsetY: Float) {
    constraintSet?.apply {
      getConstraint(R.id.container).layout.bottomMargin = -offsetY.toInt()
      getConstraint(R.id.bottom_navigation_view).transform.translationY = offsetY
      applyTo(binding?.root)
    }
  }

  private fun inject() {
    with(Injector) {
      inject<Context> { this@OrcaActivity }
      register(coreModule)
      register<FeedModule>(MainFeedModule(this@OrcaActivity))
      register<GalleryModule>(MainGalleryModule(navigator))
      register<PostDetailsModule>(MainPostDetailsModule(this@OrcaActivity))
      register<ProfileDetailsModule>(MainProfileDetailsModule(this@OrcaActivity))
      register<SearchModule>(MainSearchModule(navigator))
      register<SettingsModule>(MainSettingsModule(this@OrcaActivity))
      register<TermMutingModule>(MainTermMutingModule)
    }
  }

  private fun navigateOnBottomNavigationItemSelection() {
    binding?.bottomNavigationView?.setOnItemSelectedListener {
      navigateTo(it.itemId)
      true
    }
  }

  private fun navigateTo(@IdRes itemID: Int) {
    lifecycleScope.launch { BottomNavigation.navigate(navigator, itemID) }
  }

  private fun navigateToDefaultDestination() {
    binding?.bottomNavigationView?.selectedItemId = R.id.feed
  }
}
