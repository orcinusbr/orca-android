/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.app.activity.delegate

import androidx.activity.ComponentActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import br.com.orcinus.orca.app.module.feature.feed.MainFeedModule
import br.com.orcinus.orca.app.module.feature.gallery.MainGalleryModule
import br.com.orcinus.orca.app.module.feature.postdetails.MainPostDetailsModule
import br.com.orcinus.orca.app.module.feature.profiledetails.MainProfileDetailsModule
import br.com.orcinus.orca.app.module.feature.search.MainSearchModule
import br.com.orcinus.orca.app.module.feature.settings.MainSettingsModule
import br.com.orcinus.orca.app.module.feature.settings.termmuting.MainTermMutingModule
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.feature.feed.FeedModule
import br.com.orcinus.orca.feature.gallery.GalleryModule
import br.com.orcinus.orca.feature.postdetails.PostDetailsModule
import br.com.orcinus.orca.feature.profiledetails.ProfileDetailsModule
import br.com.orcinus.orca.feature.search.SearchModule
import br.com.orcinus.orca.feature.settings.SettingsModule
import br.com.orcinus.orca.feature.settings.termmuting.TermMutingModule
import br.com.orcinus.orca.std.injector.Injector

internal interface Injection {
  fun inject(activity: FragmentActivity, coreModule: CoreModule) {
    Injector.register(coreModule)
    Injector.register<FeedModule>(MainFeedModule(activity))
    Injector.register<GalleryModule>(MainGalleryModule)
    Injector.register<PostDetailsModule>(MainPostDetailsModule(activity))
    Injector.register<ProfileDetailsModule>(MainProfileDetailsModule(activity))
    Injector.register<SearchModule>(MainSearchModule)
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
