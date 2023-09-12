package com.jeanbarrossilva.orca.platform.theme.extensions

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow

/** [MutableInteractionSource] that doesn't emit the [Interaction]s that are sent to it. **/
@Suppress("FunctionName")
fun EmptyMutableInteractionSource(): MutableInteractionSource {
    return object : MutableInteractionSource {
        override val interactions = emptyFlow<Interaction>()

        override suspend fun emit(interaction: Interaction) {
        }

        override fun tryEmit(interaction: Interaction): Boolean {
            return false
        }
    }
}

/**
 * [MutableInteractionSource] that ignores all specified [Interaction]s.
 *
 * @param ignored [KClass]es of the [Interaction]s to be ignored.
 **/
@Suppress("FunctionName")
internal fun IgnoringMutableInteractionSource(vararg ignored: KClass<out Interaction>):
    MutableInteractionSource {
    return object : MutableInteractionSource {
        /** Whether this [Interaction] is ignored. **/
        private val Interaction.isIgnored
            get() = ignored.any(this::class::isSubclassOf)

        override val interactions = MutableSharedFlow<Interaction>()

        override suspend fun emit(interaction: Interaction) {
            if (!interaction.isIgnored) {
                this.interactions.emit(interaction)
            }
        }

        override fun tryEmit(interaction: Interaction): Boolean {
            return !interaction.isIgnored && interactions.tryEmit(interaction)
        }
    }
}
