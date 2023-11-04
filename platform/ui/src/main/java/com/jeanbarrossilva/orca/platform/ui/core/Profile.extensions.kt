package com.jeanbarrossilva.orca.platform.ui.core

import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.feed.profile.createSample
import com.jeanbarrossilva.orca.core.sample.instance.createSample
import com.jeanbarrossilva.orca.platform.ui.component.avatar.createSample
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

/** Creates a platform-specific sample [Profile]. */
@Composable
internal fun Profile.Companion.createSample(): Profile {
  val imageLoaderProvider = ImageLoader.Provider.createSample()
  val tootProvider = Instance.createSample(imageLoaderProvider).tootProvider
  return Profile.createSample(tootProvider, imageLoaderProvider)
}
