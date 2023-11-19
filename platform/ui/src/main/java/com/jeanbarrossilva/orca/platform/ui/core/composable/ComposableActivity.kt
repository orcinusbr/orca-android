package com.jeanbarrossilva.orca.platform.ui.core.composable

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.core.lifecycle.CompleteLifecycleActivity

/**
 * [Activity] that shows [Composable] content.
 *
 * @see Content
 */
abstract class ComposableActivity : CompleteLifecycleActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent { AutosTheme { Content() } }
  }

  /** Content to be shown inside the [ComposeView]. */
  @Composable protected abstract fun Content()
}
