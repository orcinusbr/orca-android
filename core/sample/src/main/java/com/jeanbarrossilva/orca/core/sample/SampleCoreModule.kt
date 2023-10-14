package com.jeanbarrossilva.orca.core.sample

import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.injector.module.Inject
import com.jeanbarrossilva.orca.std.injector.module.Module

open class SampleCoreModule(
  @Inject internal val imageLoaderProvider: Module.() -> ImageLoader.Provider<SampleImageSource>
) : Module()
