package com.jeanbarrossilva.orca.app

import android.content.ComponentCallbacks
import org.koin.android.ext.android.getKoin

/** Whether Koin has already been initialized. **/
internal val ComponentCallbacks.isKoinInitialized
    get() = try {
        getKoin()
        true
    } catch (_: IllegalStateException) {
        false
    }
