package com.jeanbarrossilva.orca.app.test

import com.jeanbarrossilva.orca.app.OrcaActivity
import com.jeanbarrossilva.orca.platform.ui.test.core.requestFocus

internal class TestOrcaActivity : OrcaActivity() {
  override fun onWindowFocusChanged(hasFocus: Boolean) {
    super.onWindowFocusChanged(hasFocus)
    requestFocus(hasFocus)
  }
}
