package com.jeanbarrossilva.orca.platform.ui.component.timeline.refresh

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.SemanticsPropertyKey

/** [SemanticsPropertyKey] returned by [InProgress]. */
private val inProgressSemanticsPropertyKey = SemanticsPropertyKey<Boolean>(name = "InProgress")

/**
 * [SemanticsPropertyKey] that indicates whether the node is currently in a temporarily active
 * state.
 */
@Suppress("UnusedReceiverParameter")
val SemanticsProperties.InProgress
  get() = inProgressSemanticsPropertyKey
