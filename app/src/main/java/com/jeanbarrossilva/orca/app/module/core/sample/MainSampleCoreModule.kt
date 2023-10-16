package com.jeanbarrossilva.orca.app.module.core.sample

import com.jeanbarrossilva.orca.app.module.core.sample.imageloader.SampleImageLoader
import com.jeanbarrossilva.orca.core.sample.SampleCoreModule
import com.jeanbarrossilva.orca.std.injector.Injector

class MainSampleCoreModule :
  SampleCoreModule({ SampleImageLoader.Provider(context = Injector.get()) })
