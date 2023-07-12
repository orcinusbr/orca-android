package com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.SemanticsPropertyKey

/** [SemanticsPropertyKey] returned by [Favorite]. **/
private val favoriteSemanticProperty = SemanticsPropertyKey<Boolean>("Favorite")

/** [SemanticsPropertyKey] for nodes that can be in a "favorite" state. **/
@Suppress("UnusedReceiverParameter")
val SemanticsProperties.Favorite
    get() = favoriteSemanticProperty
