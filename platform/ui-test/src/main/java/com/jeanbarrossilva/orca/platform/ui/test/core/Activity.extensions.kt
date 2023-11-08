package com.jeanbarrossilva.orca.platform.ui.test.core

import android.app.Activity
import android.view.View
import androidx.core.view.children
import com.jeanbarrossilva.orca.platform.ui.core.content

/**
 * Requests this [Activity] to be focused if it isn't.
 *
 * @param isFocused Whether this [Activity] is currently focused.
 * @throws IllegalStateException If this [Activity] isn't focused but doesn't have any content
 *   [View]s.
 * @see Activity.addContentView
 * @see Activity.setContentView
 */
fun Activity.requestFocus(isFocused: Boolean) {
  if (!isFocused) {
    content.children.firstOrNull()?.requestFocus()
      ?: throw IllegalStateException("Cannot request focus to an Activity without content views.")
  }
}
