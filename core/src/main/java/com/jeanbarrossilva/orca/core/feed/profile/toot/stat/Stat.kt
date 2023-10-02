package com.jeanbarrossilva.orca.core.feed.profile.toot.stat

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow

/**
 * Specific statistic whose amounts emitted to the [countFlow] doesn't necessarily reflect the
 * summed [size][List.size] of all [List]s emitted to the result of [get]. Although all core
 * variants should be as precise as possible when defining what the total amount of elements counted
 * by this [Stat] is, there is no precise and efficient way of guaranteeing parity.
 *
 * An instance of this class can be created via the [Stat] method, through which it can be
 * properly configured.
 *
 * @param count Initial amount of elements.
 * @see get
 **/
abstract class Stat<T> internal constructor(count: Int) {
    /**
     * [MutableStateFlow] that keeps track of the total amount of elements comprehended by this
     * [Stat].
     **/
    internal val countMutableFlow = MutableStateFlow(count)

    /** [StateFlow] to which the amount of elements will be emitted. **/
    val countFlow = countMutableFlow.asStateFlow()

    /** Current amount of elements. **/
    val count
        get() = countFlow.value

    /**
     * Allows for a [Stat] to be configured and built.
     *
     * @param count Initial amount of elements of the [Stat].
     **/
    open class Builder<T> internal constructor(protected val count: Int) {
        /** Lambda that provides the [Flow] to be returned by the built [Stat]'s [onGet] method. **/
        protected var onGet = { _: Int -> emptyFlow<List<T>>() }

        /**
         * Defines the [Flow] to be returned when the [Stat]'s [get] method is called.
         *
         * @param get Provides the [Flow] to be returned.
         **/
        fun get(get: (page: Int) -> Flow<List<T>>): Builder<T> {
            return apply {
                onGet = get
            }
        }

        /** Builds the [Stat] with the provided configuration. **/
        internal open fun build(): Stat<T> {
            return object : Stat<T>(count) {
                override fun get(page: Int): Flow<List<T>> {
                    return onGet.invoke(page)
                }
            }
        }
    }

    /**
     * Gets the [Flow] to which the elements related to this [Stat] will be emitted.
     *
     * @param page Page at which the elements to be emitted are.
     **/
    abstract fun get(page: Int): Flow<List<T>>
}
