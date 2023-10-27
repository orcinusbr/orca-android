package com.jeanbarrossilva.orca.platform.theme.kit.input.text.error

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList

/** Messages of the errors that have been announced by this [ErrorDispatcher]. */
internal val ErrorDispatcher.messages: SnapshotStateList<String>
  @Composable
  get() {
    val errors = remember(this) { mutableStateListOf<String>() }
    val onAnnouncementListener =
      ErrorDispatcher.OnAnnouncementListener {
        errors.clear()
        errors.addAll(it)
      }

    DisposableEffect(this) {
      listen(onAnnouncementListener)
      onDispose { remove(onAnnouncementListener) }
    }

    return errors
  }

/**
 * Remembers an [ErrorDispatcher].
 *
 * @param build Configures the [ErrorDispatcher] to be built and returned through its
 *   [ErrorDispatcher.Builder].
 */
@Composable
fun rememberErrorDispatcher(build: ErrorDispatcher.Builder.() -> Unit = {}): ErrorDispatcher {
  val errorDispatcher = remember { buildErrorDispatcher(build) }
  DisposableEffect(Unit) { onDispose(errorDispatcher::reset) }
  return errorDispatcher
}

/**
 * Builds an [ErrorDispatcher].
 *
 * @param build Configures the [ErrorDispatcher] to be built and returned through its
 *   [ErrorDispatcher.Builder].
 */
internal fun buildErrorDispatcher(build: ErrorDispatcher.Builder.() -> Unit = {}): ErrorDispatcher {
  return ErrorDispatcher.Builder().apply(build).build()
}
