package com.jeanbarrossilva.orca.app

import android.app.Application
import net.time4j.android.ApplicationStarter

internal open class OrcaApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    ApplicationStarter.initialize(this, true)
  }
}
