package com.jeanbarrossilva.orca.platform.ui.component.timeline.post

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import kotlinx.coroutines.flow.emptyFlow

/** [MutableInteractionSource] that doesn't emit the [Interaction]s that are sent to it. */
@Suppress("FunctionName")
internal fun EmptyMutableInteractionSource(): MutableInteractionSource {
  return object : MutableInteractionSource {
    override val interactions = emptyFlow<Interaction>()

    override suspend fun emit(interaction: Interaction) {}

    override fun tryEmit(interaction: Interaction): Boolean {
      return false
    }
  }
}
