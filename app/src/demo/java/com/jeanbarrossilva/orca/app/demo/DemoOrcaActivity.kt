package com.jeanbarrossilva.orca.app.demo

import com.jeanbarrossilva.orca.app.OrcaActivity
import com.jeanbarrossilva.orca.app.demo.module.core.DemoHttpModule

internal class DemoOrcaActivity : OrcaActivity() {
  override val httpModule = DemoHttpModule
}
