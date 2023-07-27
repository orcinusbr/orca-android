package com.jeanbarrossilva.mastodonte.platform.ui.core

import android.content.Intent
import android.os.Bundle

/**
 * Requires this [Intent]'s [extras][Intent.getExtras].
 *
 * @throws IllegalStateException If its [extras][Intent.getExtras] are `null`.
 **/
internal fun Intent.requireExtras(): Bundle {
    return extras ?: throw IllegalStateException("$this has no extras.")
}
