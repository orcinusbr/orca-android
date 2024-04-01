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

package com.jeanbarrossilva.orca.platform.autos.kit.input.text.error

import android.content.Context
import androidx.annotation.StringRes

/**
 * Coordinates dynamic error triggering.
 *
 * @param errors [Error]s that have been registered.
 */
class ErrorDispatcher private constructor(private val errors: List<Error>) {
  /** [OnAnnouncementListener]s that are currently active. */
  private val onAnnouncementListeners = mutableListOf<OnAnnouncementListener>()

  /** Text that's been registered before dispatching. */
  private var preDispatchText = ""

  /** Whether the [errors] have already been dispatched. */
  internal var hasDispatched = false

  /** Whether the most recently registered text contains errors. */
  internal var containsErrors = false

  /**
   * Invalid text state.
   *
   * @param message [String] that describes why the text is invalid.
   * @param condition Returns whether the given text is invalid.
   */
  private data class Error(val message: String, val condition: (text: String) -> Boolean)

  /** Listens to error announcements. */
  fun interface OnAnnouncementListener {
    /**
     * Callback called whenever errors are announced.
     *
     * @param messages Messages of the errors that are being announced.
     */
    fun onAnnouncement(messages: List<String>)
  }

  /** Configures and builds an [ErrorDispatcher]. */
  class Builder internal constructor() {
    /** [Error]s with which the [ErrorDispatcher] will be built. */
    private val errors = mutableListOf<Error>()

    /**
     * Adds an error to be shown when validating input text.
     *
     * @param context [Context] through which the message will be obtained through its
     *   [messageResourceID].
     * @param messageResourceID Resource ID of the [String] that describes the error.
     * @param condition Returns whether the given text is invalid, and, therefore, that the error
     *   should be shown.
     */
    fun error(
      context: Context,
      @StringRes messageResourceID: Int,
      condition: (text: String) -> Boolean
    ) {
      val message = context.getString(messageResourceID)
      error(message, condition)
    }

    /**
     * Adds an error to be shown when validating input text.
     *
     * @param message [String] that describes the error.
     * @param condition Returns whether the given text is invalid and, therefore, that the error
     *   should be shown.
     */
    fun error(message: String, condition: (text: String) -> Boolean) {
      val error = Error(message, condition)
      errors.add(error)
    }

    /**
     * Adds an error to be shown at all times when validating text, considering the input to always
     * be invalid.
     *
     * @param message [String] that describes the error.
     */
    fun errorAlways(message: String) {
      error(message) { true }
    }

    /** Builds an [ErrorDispatcher] with the provided configuration. */
    internal fun build(): ErrorDispatcher {
      val errorsAsList = errors.toList()
      return ErrorDispatcher(errorsAsList)
    }
  }

  /**
   * Triggers error announcements for the [preDispatchText] if this [ErrorDispatcher] hasn't already
   * dispatched and enables them to be listened to.
   */
  fun dispatch() {
    if (!hasDispatched) {
      announceErrors(preDispatchText)
      hasDispatched = true
    }
  }

  /**
   * Notifies the [onAnnouncementListener] of error announcements.
   *
   * @param onAnnouncementListener [OnAnnouncementListener] to be notified.
   */
  fun listen(onAnnouncementListener: OnAnnouncementListener) {
    onAnnouncementListeners.add(onAnnouncementListener)
  }

  /**
   * Registers the given [text] and announce its errors if this [ErrorDispatcher] has already
   * dispatched.
   *
   * @param text Input to be registered.
   */
  fun register(text: String) {
    if (hasDispatched) {
      announceErrors(text)
    } else {
      preDispatchText = text
    }
  }

  /**
   * Runs the [usage] lambda and then resets this [ErrorDispatcher].
   *
   * @param T Value returned by the [usage].
   * @param usage Operations to be performed that precede the reset.
   */
  internal fun <T> use(usage: (ErrorDispatcher) -> T): T {
    try {
      return usage(this)
    } finally {
      reset()
    }
  }

  /** Sets this [ErrorDispatcher] to its initial state. */
  internal fun reset() {
    onAnnouncementListeners.clear()
    preDispatchText = ""
    hasDispatched = false
  }

  /**
   * Removes the currently active [onAnnouncementListener].
   *
   * @param onAnnouncementListener [OnAnnouncementListener] to be removed.
   */
  internal fun remove(onAnnouncementListener: OnAnnouncementListener) {
    onAnnouncementListeners.remove(onAnnouncementListener)
  }

  /**
   * Announces the errors for the given [text] to the [onAnnouncementListeners].
   *
   * @param text Input for which errors will be announced (if any).
   */
  private fun announceErrors(text: String) {
    val messages = errors.filter { it.condition(text) }.map(Error::message)
    containsErrors = messages.isNotEmpty()
    onAnnouncementListeners.forEach { it.onAnnouncement(messages) }
  }
}
