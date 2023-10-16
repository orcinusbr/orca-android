package com.jeanbarrossilva.orca.app.demo

import com.jeanbarrossilva.orca.app.OrcaActivity
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.std.injector.Injector

internal class DemoOrcaActivity : OrcaActivity() {
  override fun Injector.registerCoreModule() {
    val coreModule = sampleCoreModule
    register<CoreModule>(coreModule)
  }
}
