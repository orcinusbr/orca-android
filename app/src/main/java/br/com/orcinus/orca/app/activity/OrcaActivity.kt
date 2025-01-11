/*
 * Copyright © 2023–2025 Orcinus
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

package br.com.orcinus.orca.app.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import androidx.annotation.CallSuper
import androidx.annotation.Discouraged
import androidx.core.view.WindowCompat
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import br.com.orcinus.orca.app.R
import br.com.orcinus.orca.app.activity.masking.Masker
import br.com.orcinus.orca.app.databinding.ActivityOrcaBinding
import br.com.orcinus.orca.app.module.core.MastodonCoreModule
import br.com.orcinus.orca.app.module.feature.composer.MainComposerModule
import br.com.orcinus.orca.app.module.feature.feed.MainFeedModule
import br.com.orcinus.orca.app.module.feature.gallery.MainGalleryModule
import br.com.orcinus.orca.app.module.feature.postdetails.MainPostDetailsModule
import br.com.orcinus.orca.app.module.feature.search.MainSearchModule
import br.com.orcinus.orca.app.module.feature.settings.MainSettingsModule
import br.com.orcinus.orca.app.module.feature.settings.termmuting.MainTermMutingModule
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.module.authenticationLock
import br.com.orcinus.orca.feature.composer.ComposerModule
import br.com.orcinus.orca.feature.feed.FeedModule
import br.com.orcinus.orca.feature.gallery.GalleryModule
import br.com.orcinus.orca.feature.postdetails.PostDetailsModule
import br.com.orcinus.orca.feature.profiledetails.ProfileDetailsModule
import br.com.orcinus.orca.feature.search.SearchModule
import br.com.orcinus.orca.feature.settings.SettingsModule
import br.com.orcinus.orca.feature.settings.termmuting.TermMutingModule
import br.com.orcinus.orca.platform.navigation.BackStack
import br.com.orcinus.orca.platform.navigation.Navigator
import br.com.orcinus.orca.std.injector.Injector
import br.com.orcinus.orca.std.injector.module.Module
import kotlinx.coroutines.launch

/**
 * Main [Activity] that gets started when the app launches.
 *
 * It is primarily intended for configuring state which requires a pre-lifecycle-ending
 * deconfiguration (in this case, such "teardown" should be performed in [onDestroy]) and/or is
 * directly intrinsic to the [Window].
 *
 * @param C [CoreModule] to be injected.
 * @see getWindow
 * @see onDestroy
 */
internal abstract class OrcaActivity<C : CoreModule> : FragmentActivity() {
  /**
   * [ActivityOrcaBinding] containing references to the [View]s specified in the layout XML file.
   * Gets assigned a non-null value on creation and is nullified after destruction.
   */
  private var binding: ActivityOrcaBinding? = null

  /** [CoreModule] created via [createCoreModule]. */
  @Discouraged("Might not have been created yet. Prefer calling createOrGetCoreModule().")
  private lateinit var createdCoreModule: C

  /** [ProfileDetailsModule] created via [createProfileDetailsModule]. */
  @Discouraged("Might not have been created yet. Prefer calling createOrGetCoreModule().")
  private lateinit var createdProfileDetailsModule: ProfileDetailsModule

  /**
   * Alias for `createOrGetCoreModule()`.
   *
   * @see createOrGetCoreModule
   */
  @Deprecated(
    "Prefer calling createOrGetCoreModule() for clarity.",
    ReplaceWith("createOrGetCoreModule()")
  )
  inline val coreModule
    get() = createOrGetCoreModule()

  /**
   * Alias for `createOrGetProfileDetailsModule()`.
   *
   * @see createOrGetProfileDetailsModule
   */
  @Deprecated(
    "Prefer calling createOrGetProfileDetailsModule() for clarity.",
    ReplaceWith("createOrGetProfileDetailsModule()")
  )
  inline val profileDetailsModule
    get() = createOrGetProfileDetailsModule()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    binding = ActivityOrcaBinding.inflate(layoutInflater)
    setContentView(binding?.root)
    inject()
    navigateOnBottomNavigationItemSelection()
    selectDefaultBottomNavigationItem()
  }

  override fun setContentView(view: View?) {
    super.setContentView(view)
    view?.doOnLayout { binding?.containerLayout?.let(Masker::mask) }
  }

  override fun onDestroy() {
    super.onDestroy()
    binding = null
    Injector.clear()
  }

  /** Injects this [OrcaActivity] as a UI [Context] and registers feature [Module]s. */
  @CallSuper
  open fun inject() {
    Injector.injectLazily<Context> { this@OrcaActivity }
    Injector.register<CoreModule>(coreModule)
    Injector.register<ComposerModule>(MainComposerModule(lifecycleScope))
    Injector.register<FeedModule>(MainFeedModule(this))
    Injector.register<GalleryModule>(MainGalleryModule)
    Injector.register<PostDetailsModule>(MainPostDetailsModule(this))
    Injector.register(createOrGetProfileDetailsModule())
    Injector.register<SearchModule>(MainSearchModule)
    Injector.register<SettingsModule>(MainSettingsModule(this))
    Injector.register<TermMutingModule>(MainTermMutingModule)
  }

  /**
   * Either creates the [CoreModule] in case it has not been created yet or obtains the previously
   * created one.
   *
   * @see createCoreModule
   */
  @Suppress("DiscouragedApi")
  fun createOrGetCoreModule() =
    if (::createdCoreModule.isInitialized) {
      createdCoreModule
    } else {
      createdCoreModule = createCoreModule()
      createdCoreModule
    }

  /**
   * Either creates the [ProfileDetailsModule] in case it has not been created yet or obtains the
   * previously created one.
   *
   * @see createProfileDetailsModule
   */
  @Suppress("DiscouragedApi")
  fun createOrGetProfileDetailsModule() =
    if (::createdProfileDetailsModule.isInitialized) {
      createdProfileDetailsModule
    } else {
      createdProfileDetailsModule = createProfileDetailsModule()
      createdProfileDetailsModule
    }

  /**
   * Creates the [CoreModule] to be registered. Overridability is useful because the application can
   * be built with distinct flavors with different core structures (e. g., in the default, main
   * version, ones that access the API through network requests; in the demo version, ones that
   * store and retrieve data locally).
   *
   * @see Injector.register
   * @see MastodonCoreModule
   */
  protected abstract fun createCoreModule(): C

  /**
   * Creates the [Module] into which the dependencies required by the profile details feature are
   * injected.
   */
  protected abstract fun createProfileDetailsModule(): ProfileDetailsModule

  /**
   * Listens to selections on each of the bottom navigation [MenuItem]s and navigates to their
   * respective [Fragment] when they are performed. Each [MenuItem] is considered to be selected
   * unconditionally afterwards.
   */
  private fun navigateOnBottomNavigationItemSelection() {
    binding?.bottomNavigationView?.setOnItemSelectedListener {
      navigate(it)
      true
    }
  }

  /**
   * Navigates to the [Fragment] associated to the [item].
   *
   * @param item [MenuItem] that is considered to have been selected and to whose associated
   *   [Fragment] navigation will be performed.
   */
  private fun navigate(item: MenuItem) {
    val itemTitle = item.title?.toString() ?: return
    val backStack = BackStack.named(itemTitle)
    val navigator = Navigator.create(this, backStack)
    lifecycleScope.launch {
      BottomNavigationFragmentProvider.navigate(
        navigator,
        backStack,
        coreModule.authenticationLock(),
        item.itemId
      )
    }
  }

  /** Selects the bottom navigation [MenuItem] that is considered to be the default one. */
  private fun selectDefaultBottomNavigationItem() {
    binding?.bottomNavigationView?.selectedItemId = R.id.feed
  }
}
