package com.jeanbarrossilva.orca.platform.ui.core.context

import android.content.Context
import com.jeanbarrossilva.orca.platform.ui.core.Intent

/**
 * Opens the share sheet so that the [text] can be shared.
 *
 * @param text Text to be shared.
 */
fun Context.share(text: String) {
  val intent = Intent(text)
  startActivity(intent)
}
