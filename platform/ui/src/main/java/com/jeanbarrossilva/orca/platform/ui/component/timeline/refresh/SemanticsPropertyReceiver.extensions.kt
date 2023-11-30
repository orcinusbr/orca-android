package com.jeanbarrossilva.orca.platform.ui.component.timeline.refresh

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.SemanticsPropertyReceiver

/** Whether the node is currently in a temporarily active state. */
internal var SemanticsPropertyReceiver.isInProgress by SemanticsProperties.InProgress
