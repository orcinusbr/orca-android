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

package br.com.orcinus.orca.core.sample.instance

import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.instance.InstanceProvider
import br.com.orcinus.orca.core.sample.image.SampleImageSource
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider

/**
 * [InstanceProvider] that provides a [SampleInstance] with default [Profile]s and [Post]s.
 *
 * @see provide
 */
abstract class SampleInstanceProvider internal constructor() : InstanceProvider {
  /** [SampleInstance] that is set once and provided. */
  private lateinit var instance: SampleInstance

  override fun provide(): SampleInstance {
    return if (::instance.isInitialized) {
      instance
    } else {
      instance = onProvisioning()
      instance
    }
  }

  /**
   * Lazily creates the [SampleInstance] to be provided, which is then returned by all subsequent
   * provisioning requests.
   */
  protected abstract fun onProvisioning(): SampleInstance
}

/**
 * Creates a [SampleInstanceProvider] whose provided [SampleInstance] contains default [Profile]s
 * and [Post]s.
 *
 * @param imageLoaderProvider Provides the [ImageLoader] by which images are to be loaded.
 */
fun SampleInstanceProvider(
  imageLoaderProvider: SomeImageLoaderProvider<SampleImageSource>
): SampleInstanceProvider {
  return SampleInstanceProvider {
    SampleInstance.Builder.create(imageLoaderProvider)
      .withDefaultProfiles()
      .withDefaultPosts()
      .build()
  }
}

/**
 * Creates a [SampleInstanceProvider].
 *
 * @param onProvisioning Lazily creates the [SampleInstance] to be provided, which is then returned
 *   by all subsequent provisioning requests.
 */
fun SampleInstanceProvider(onProvisioning: () -> SampleInstance): SampleInstanceProvider {
  return object : SampleInstanceProvider() {
    override fun onProvisioning(): SampleInstance {
      return onProvisioning()
    }
  }
}
