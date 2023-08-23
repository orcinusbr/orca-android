package com.jeanbarrossilva.orca.cache

import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

/** Decides whether values should be fetched or have their cached version retrieved. **/
abstract class Cache<K, V> {
    /**
     * Holds the keys of values that are currently idle, associated to the current [elapsedTime] of
     * the moment in which the idling was performed.
     *
     * @see timeToIdle
     **/
    private val idling = HashMap<K, Duration>()

    /**
     * Holds the keys of values that are currently alive, associated to the current [elapsedTime] of
     * the moment in which they were marked as alive.
     *
     * @see timeToLive
     * @see markAsAlive
     **/
    private val living = HashMap<K, Duration>()

    /** Elapsed time provided by the [elapsedTimeProvider]. **/
    private val elapsedTime
        get() = elapsedTimeProvider.provide()

    /** [ElapsedTimeProvider] for accessing the current elapsed time. **/
    internal open val elapsedTimeProvider = ElapsedTimeProvider.system

    /** [Fetcher] through which values will be obtained from their source, normally the network. **/
    protected abstract val fetcher: Fetcher<K, V>

    /** [Storage] for fetched values to be stored in and retrieved from. **/
    protected abstract val storage: Storage<K, V>

    /**
     * Time-to-idle is the expiration threshold related to the last time a value has been read or
     * written.
     **/
    protected open val timeToIdle: Duration = 1.days

    /** Time-to-live is similar to [timeToIdle], but only considers write operations. **/
    protected open val timeToLive: Duration = 30.minutes

    /** Provides the amount of time that has passed through [provide]. **/
    internal fun interface ElapsedTimeProvider {
        /** Provides the amount of time that has passed. **/
        fun provide(): Duration

        companion object {
            /** [ElapsedTimeProvider] that provides the system's current time in [milliseconds]. **/
            val system = ElapsedTimeProvider {
                System.currentTimeMillis().milliseconds
            }
        }
    }

    /**
     * Gets the value bound to the given [key] either by retrieving it from the [storage] if it's
     * been cached or fetches them through the [fetcher] if it hasn't, respecting both the
     * [timeToIdle] and the [timeToLive].
     *
     * @param key Unique identifier to which the value to be obtained is associated to.
     **/
    suspend fun get(key: K): V {
        val isUpToDate = isIdle(key) && isAlive(key)
        return if (isUpToDate) storage.get(key) else remember(key)
    }

    /**
     * Adds the [key] to [idling], bound to the current [elapsedTime].
     *
     * @param key Unique identifier of the value to be marked as idle.
     * @see timeToIdle
     **/
    private fun markAsIdle(key: K) {
        idling[key] = elapsedTime
    }

    /**
     * Adds the [key] to [living], bound to the current [elapsedTime].
     *
     * @param key Unique identifier of the value to be marked as idle.
     * @see timeToLive
     **/
    private fun markAsAlive(key: K) {
        living[key] = elapsedTime
    }

    /**
     * Returns whether the value associated to the given [key] is idle, and also unmarks it as such
     * if it isn't.
     *
     * @param key Unique identifier of the value.
     * @see timeToIdle
     **/
    private suspend fun isIdle(key: K): Boolean {
        val isStored = storage.contains(key)
        val isPastTimeToIdle = isStored && elapsedTime since idling.getValue(key) >= timeToIdle
        if (isPastTimeToIdle) {
            unmarkAsIdle(key)
        }
        return isStored && !isPastTimeToIdle
    }

    /**
     * Removes the [key] and its associated value from [idling].
     *
     * @param key Unique identifier of the value to be unmarked as idle.
     * @see timeToIdle
     **/
    private fun unmarkAsIdle(key: K) {
        idling.remove(key)
    }

    /**
     * Returns whether the value associated to the given [key] is alive, and also unmarks it as such
     * if it isn't.
     *
     * @param key Unique identifier of the value.
     * @see timeToLive
     **/
    private suspend fun isAlive(key: K): Boolean {
        val isStored = storage.contains(key)
        val isPastTimeToLive = isStored && elapsedTime since living.getValue(key) >= timeToLive
        if (isPastTimeToLive) {
            unmarkAsAlive(key)
        }
        return isStored && !isPastTimeToLive
    }

    /**
     * Removes the [key] and its associated value from both [living] and the [storage].
     *
     * @param key Unique identifier of the value to be unmarked as alive.
     * @see timeToLive
     **/
    private suspend fun unmarkAsAlive(key: K) {
        storage.remove(key)
        living.remove(key)
    }

    /**
     * Fetches the value associated to the given [key] and stores it, marking it as both idle and
     * alive.
     *
     * @param key Unique identifier of the value to be fetched and stored.
     * @return Value that's been fetched/stored.
     * @see markAsIdle
     * @see markAsAlive
     **/
    private suspend fun remember(key: K): V {
        val value = fetcher.fetch(key)
        storage.store(key, value)
        markAsIdle(key)
        markAsAlive(key)
        return value
    }
}
