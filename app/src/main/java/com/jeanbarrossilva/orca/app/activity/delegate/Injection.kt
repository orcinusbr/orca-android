/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.app.activity.delegate

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.jeanbarrossilva.orca.app.module.feature.feed.MainFeedModule
import com.jeanbarrossilva.orca.app.module.feature.gallery.MainGalleryModule
import com.jeanbarrossilva.orca.app.module.feature.postdetails.MainPostDetailsModule
import com.jeanbarrossilva.orca.app.module.feature.profiledetails.MainProfileDetailsModule
import com.jeanbarrossilva.orca.app.module.feature.search.MainSearchModule
import com.jeanbarrossilva.orca.app.module.feature.settings.MainSettingsModule
import com.jeanbarrossilva.orca.app.module.feature.settings.termmuting.MainTermMutingModule
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.feature.feed.FeedModule
import com.jeanbarrossilva.orca.feature.gallery.GalleryModule
import com.jeanbarrossilva.orca.feature.postdetails.PostDetailsModule
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetailsModule
import com.jeanbarrossilva.orca.feature.search.SearchModule
import com.jeanbarrossilva.orca.feature.settings.SettingsModule
import com.jeanbarrossilva.orca.feature.settings.termmuting.TermMutingModule
import com.jeanbarrossilva.orca.platform.navigation.NavigationActivity
import com.jeanbarrossilva.orca.std.injector.Injector

internal interface Injection {
  fun inject(activity: NavigationActivity, coreModule: CoreModule) {
    Injector.inject<Context> { activity }
    Injector.register(coreModule)
    Injector.register<FeedModule>(MainFeedModule(activity))
    Injector.register<GalleryModule>(MainGalleryModule(activity.navigator))
    Injector.register<PostDetailsModule>(MainPostDetailsModule(activity))
    Injector.register<ProfileDetailsModule>(MainProfileDetailsModule(activity))
    Injector.register<SearchModule>(MainSearchModule(activity.navigator))
    Injector.register<SettingsModule>(MainSettingsModule(activity))
    Injector.register<TermMutingModule>(MainTermMutingModule)
    clearDependenciesOnDestroy(activity)
  }

  private fun clearDependenciesOnDestroy(activity: ComponentActivity) {
    activity.lifecycle.addObserver(
      object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
          Injector.clear()
        }
      }
    )
  }
}
