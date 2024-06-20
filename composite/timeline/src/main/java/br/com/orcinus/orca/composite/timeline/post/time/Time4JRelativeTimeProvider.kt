/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.composite.timeline.post.time

import android.content.BroadcastReceiver
import android.content.Context
import br.com.orcinus.orca.ext.reflection.access
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.KProperty0
import kotlin.reflect.full.staticProperties
import net.time4j.PrettyTime
import net.time4j.android.ApplicationStarter
import net.time4j.android.spi.AndroidResourceLoader
import net.time4j.base.UnixTime
import net.time4j.tz.Timezone

/**
 * [RelativeTimeProvider] that provides relative time with Time4J.
 *
 * @property context [Context] through which Time4J will be started the first time a relative time
 *   is requested to be provided.
 */
internal class Time4JRelativeTimeProvider(private val context: Context) : RelativeTimeProvider() {
  /** Whether Time4J has been started. */
  private var hasTime4JBeenStarted = false

  /**
   * Whether the [AndroidResourceLoader] for accessing internal asset files has been initialized and
   * is prepared for use.
   *
   * @throws NoSuchFieldException If the private, static `PREPARED` [AtomicBoolean] isn't found via
   *   reflection in the [ApplicationStarter] class.
   * @see AndroidResourceLoader.init
   */
  private val isPrepared
    @Throws(NoSuchFieldException::class) get() = getWhetherIs("PREPARED")

  /**
   * Whether the [BroadcastReceiver] that refreshes the [Timezone.Cache] when the system timezone
   * changes has been registered into the [context] by Time4J.
   *
   * @throws NoSuchFieldException If the private, static `REGISTERED` [AtomicBoolean] isn't found
   *   via reflection in the [ApplicationStarter] class.
   * @see Timezone.Cache.refresh
   */
  private val isRegistered
    @Throws(NoSuchFieldException::class) get() = getWhetherIs("REGISTERED")

  override suspend fun onProvide(dateTime: ZonedDateTime): String {
    val locale = Locale.getDefault()
    val unixTime = dateTime.toUnixTime()
    val timeZoneID = ZoneId.systemDefault().id
    startTime4J()
    return PrettyTime.of(locale).printRelative(unixTime, timeZoneID)
  }

  /**
   * Finds the [AtomicBoolean] static property belonging to the [ApplicationStarter] and gets the
   * [Boolean] set to its assigned value.
   *
   * @param propertyName Name of the [KProperty0] whose underlying value is to be returned.
   * @throws NoSuchFieldException If such a property named [propertyName] isn't found.
   * @see AtomicBoolean.get
   */
  @Throws(NoSuchFieldException::class)
  private fun getWhetherIs(propertyName: String): Boolean {
    return ApplicationStarter::class
      .staticProperties
      .filterIsInstance<KProperty0<AtomicBoolean>>()
      .singleOrNull { it.name == propertyName }
      ?.access { get().get() }
      ?: throw NoSuchFieldException("${ApplicationStarter::class.qualifiedName}.$propertyName")
  }

  /** Converts this [ZonedDateTime] into a [UnixTime]. */
  private fun ZonedDateTime.toUnixTime(): UnixTime {
    return object : UnixTime {
      override fun getPosixTime(): Long {
        return toEpochSecond()
      }

      override fun getNanosecond(): Int {
        return nano
      }
    }
  }

  /** Starts Time4J if it hasn't been started yet. */
  private suspend fun startTime4J() {
    if (!hasTime4JBeenStarted) {
      ApplicationStarter.initialize(context, true)
      awaitTime4JInitialization()
      hasTime4JBeenStarted = true
    }
  }

  /**
   * Blocks the execution flow until both the [AndroidResourceLoader] that accesses internal asset
   * files has been made prepared and the [BroadcastReceiver] for detecting timezone changes is
   * registered by Time4J.
   *
   * @see isPrepared
   * @see isRegistered
   */
  private suspend fun awaitTime4JInitialization() {
    suspendCoroutine {
      @Suppress("ControlFlowWithEmptyBody") while (!isRegistered || !isPrepared) {}
      it.resume(Unit)
    }
  }
}
