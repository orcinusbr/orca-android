package com.jeanbarrossilva.orca.feature.composer

import android.content.Context
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableActivity
import com.jeanbarrossilva.orca.platform.ui.core.on

class ComposerActivity internal constructor() : ComposableActivity() {
  private val viewModel by viewModels<ComposerViewModel>()

  @Composable
  override fun Content() {
    Composer(viewModel, onBackwardsNavigation = ::finish)
  }

  companion object {
    fun start(context: Context) {
      context.on<ComposerActivity>().asNewTask().start()
    }
  }
}
