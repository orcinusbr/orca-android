package com.jeanbarrossilva.mastodonte.app.demo

import com.jeanbarrossilva.mastodonte.app.MastodonteActivity
import com.jeanbarrossilva.mastodonte.app.demo.module.core.DemoCoreModule

internal class DemoMastodonteActivity : MastodonteActivity() {
    override val coreModule = DemoCoreModule()
}
