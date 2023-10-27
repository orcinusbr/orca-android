package com.jeanbarrossilva.orca.platform.theme.kit.input.text.error

/** Coordinates dynamic error triggering. */
class ErrorDispatcher internal constructor() {
  /** [Error]s that have been registered. */
  private val errors = mutableListOf<Error>()

  /** [OnAnnouncementListener]s that are currently active. */
  private val onAnnouncementListeners = mutableListOf<OnAnnouncementListener>()

  /** Text that's been registered before dispatching. */
  private var preDispatchText = ""

  /** Whether the [errors] have already been dispatched. */
  internal var hasDispatched = false

  /**
   * Invalid text state.
   *
   * @param message [String] that describes why the text is invalid.
   * @param condition Returns whether the given text is invalid.
   */
  internal data class Error(val message: String, val condition: (text: String) -> Boolean)

  /** Listens to error announcements. */
  fun interface OnAnnouncementListener {
    /**
     * Callback called whenever errors are announced.
     *
     * @param messages Messages of the errors that are being announced.
     */
    fun onAnnouncement(messages: List<String>)
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
    errors.clear()
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
    onAnnouncementListeners.forEach { it.onAnnouncement(messages) }
  }
}
