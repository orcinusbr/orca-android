/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

@file:JvmName("MutableStateFlowExtensions")

package com.jeanbarrossilva.orca.feature.postdetails.viewmodel.notifier

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Creates a [MutableStateFlow] to which [Notifier]s are emitted and that has the only purpose of
 * being collected by other [Flow]s, letting them know when it's time for them to emit new values.
 *
 * Notifications can be sent by it through [notify], which will simply emit a [Notifier] that's
 * different from the one currently being held. Because a [StateFlow] doesn't consecutively emit
 * values that are equal, there are two existing [Notifier] instances: [Notifier.initial] and
 * [Notifier.subsequent]; they're emitted interchangeably at each call to that method.
 *
 * Usage example:
 * ```kotlin
 * val notifierFlow = notifierFlow()
 * val countFlow = notifierFlow.runningFold(0) { count, _ -> count.inc() }
 * repeat(4) { notifierFlow.notify() }
 * countFlow.collect(::println)
 * ```
 *
 * In this case, `countFlow` emits an incremented version of its current value at each notification
 * `notifierFlow` sends, and the result of its collection at the end is the printing of "0", "1",
 * "2" and "3".
 *
 * @see Notifier.next
 * @see Flow.collect
 */
internal fun notifierFlow(): MutableStateFlow<Notifier> {
  return MutableStateFlow(Notifier.initial)
}

/**
 * Emits either a [Notifier.initial] or a [Notifier.subsequent] to this [MutableStateFlow] depending
 * on what its current value is.
 *
 * @see notifierFlow
 */
internal fun MutableStateFlow<Notifier>.notify() {
  value = value.next()
}
