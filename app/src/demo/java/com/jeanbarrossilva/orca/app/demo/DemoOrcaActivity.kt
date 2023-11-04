package com.jeanbarrossilva.orca.app.demo

import com.jeanbarrossilva.orca.app.OrcaActivity
import com.jeanbarrossilva.orca.app.demo.module.core.DemoCoreModule

internal open class DemoOrcaActivity : OrcaActivity() {
  override val coreModule = DemoCoreModule
}
