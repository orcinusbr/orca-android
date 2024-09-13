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

package br.com.orcinus.orca.app

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import br.com.orcinus.orca.app.activity.OrcaActivity
import br.com.orcinus.orca.app.module.core.MainMastodonCoreModule
import br.com.orcinus.orca.app.module.feature.feed.MainFeedModule
import br.com.orcinus.orca.app.module.feature.gallery.MainGalleryModule
import br.com.orcinus.orca.app.module.feature.postdetails.MainPostDetailsModule
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
import br.com.orcinus.orca.std.injector.module.Module

/**
 * Class whose instance lives throughout the whole lifecycle of the application.
 *
 * When created, some dependencies are injected, such as boundaries (interfaces to which navigation
 * from one context to another is delegated) and core structures (responsible for the main intended
 * behavior of the program); these tend to then be retrieved by features, such as the feed's or the
 * profile details'. By default, they are dejected when the running [OrcaActivity] is destroyed.
 *
 * It is primarily intended to extend the default functionality of the base [Application] class,
 * configuring state to be maintained for as long as the process is alive. Any other setting which
 * does not have this requirement should be performed in the specific context by which it is needed
 * (e. g., the [Activity] or the [Fragment] of a given feature).
 *
 * @see onCreate
 * @see Injector.inject
 * @see OrcaActivity.onDestroy
 */
internal abstract class OrcaApplication : Application() {
  /**
   * [CoreModule] to be registered. Overridability is useful because the application can be built
   * with distinct flavors with different core structures (e. g., in the default, main version, ones
   * that access the API through network requests; in the demo version, ones that store and retrieve
   * data locally).
   *
   * @see Injector.register
   * @see MainMastodonCoreModule
   */
  protected abstract val coreModule: CoreModule

  /** [Module] into which the dependencies required by the profile details feature are injected. */
  protected abstract val profileDetailsModule: ProfileDetailsModule

  override fun onCreate() {
    super.onCreate()
    Injector.injectLazily<Context> { this@OrcaApplication }
    Injector.register(coreModule)
    Injector.register<FeedModule>(MainFeedModule(this))
    Injector.register<GalleryModule>(MainGalleryModule)
    Injector.register<PostDetailsModule>(MainPostDetailsModule(this))
    Injector.register(profileDetailsModule)
    Injector.register<SearchModule>(MainSearchModule)
    Injector.register<SettingsModule>(MainSettingsModule(this))
    Injector.register<TermMutingModule>(MainTermMutingModule)
  }

  override fun onTerminate() {
    super.onTerminate()
    Injector.clear()
  }
}
