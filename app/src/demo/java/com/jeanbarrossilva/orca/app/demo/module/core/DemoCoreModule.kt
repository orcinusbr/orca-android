package com.jeanbarrossilva.orca.app.demo.module.core

import android.content.Context
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.content.SampleTermMuter
import com.jeanbarrossilva.orca.core.sample.instance.createSample
import com.jeanbarrossilva.orca.platform.ui.component.avatar.createSample
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.injector.Injector

internal object DemoCoreModule :
  CoreModule(
    instanceProvider = {
      val context = Injector.get<Context>()
      val imageLoaderProvider = ImageLoader.Provider.createSample(context)
      InstanceProvider.createSample(imageLoaderProvider)
    },
    { SampleTermMuter() }
  )
