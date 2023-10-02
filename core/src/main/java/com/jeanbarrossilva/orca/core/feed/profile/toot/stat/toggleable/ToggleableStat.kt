package com.jeanbarrossilva.orca.core.feed.profile.toot.stat.toggleable

import com.jeanbarrossilva.orca.core.feed.profile.toot.stat.Stat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * [Stat] that can have its enable-ability toggled.
 *
 * @param count Initial amount of elements.
 **/
abstract class ToggleableStat<T> internal constructor(count: Int) : Stat<T>(count) {
    /** [MutableStateFlow] that gets emitted to whenever this [ToggleableStat] is toggled. **/
    private val isEnabledMutableFlow = MutableStateFlow(false)

    /** [StateFlow] to which the current enable-ability state will be emitted. **/
    val isEnabledFlow = isEnabledMutableFlow.asStateFlow()

    /** Whether this [ToggleableStat] is currently enabled. **/
    val isEnabled
        get() = isEnabledFlow.value

    /**
     * Allows for a [ToggleableStat] to be configured and built.
     *
     * @param count Initial amount of elements of the [ToggleableStat].
     **/
    class Builder<T> internal constructor(count: Int) : Stat.Builder<T>(count) {
        /** Lambda to be invoked when the [ToggleableStat]'s [setEnabled] method is called. **/
        private var onSetEnabled: suspend (isEnabled: Boolean) -> Unit = { }

        /**
         * Defines what will be done when the [ToggleableStat]'s [setEnabled] is called.
         *
         * @param setEnabled Callback to be run.
         **/
        fun setEnabled(setEnabled: suspend (isEnabled: Boolean) -> Unit): Builder<T> {
            return apply {
                onSetEnabled = setEnabled
            }
        }

        override fun build(): ToggleableStat<T> {
            return object : ToggleableStat<T>(count) {
                override fun get(page: Int): Flow<List<T>> {
                    return onGet.invoke(page)
                }

                override suspend fun setEnabled(isEnabled: Boolean) {
                    onSetEnabled(isEnabled)
                }
            }
        }
    }

    /** Toggles whether this [ToggleableStat] is enabled. **/
    suspend fun toggle() {
        setEnabled(!isEnabled)
        isEnabledMutableFlow.value = !isEnabled
        countMutableFlow.value = if (isEnabled) countFlow.value.inc() else countFlow.value.dec()
    }

    /** Enables this [ToggleableStat]. **/
    suspend fun enable() {
        if (!isEnabled) {
            setEnabled(true)
            isEnabledMutableFlow.value = true
            countMutableFlow.value++
        }
    }

    /** Disables this [ToggleableStat]. **/
    suspend fun disable() {
        if (isEnabled) {
            setEnabled(false)
            isEnabledMutableFlow.value = false
            countMutableFlow.value--
        }
    }

    /**
     * Defines whether this [ToggleableStat] is enabled.
     *
     * @param isEnabled Whether it's being enabled or disabled.
     **/
    protected abstract suspend fun setEnabled(isEnabled: Boolean)
}
