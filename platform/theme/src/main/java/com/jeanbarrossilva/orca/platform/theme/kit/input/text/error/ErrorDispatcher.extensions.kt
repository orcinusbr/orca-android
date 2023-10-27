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

/** Remembers an [ErrorDispatcher]. */
@Composable
fun rememberErrorDispatcher(): ErrorDispatcher {
  val errorDispatcher = remember(::ErrorDispatcher)
  DisposableEffect(Unit) { onDispose(errorDispatcher::reset) }
  return errorDispatcher
}
