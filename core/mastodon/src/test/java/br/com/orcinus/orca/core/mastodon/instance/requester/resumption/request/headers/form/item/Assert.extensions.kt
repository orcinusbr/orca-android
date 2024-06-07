/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.core.mastodon.instance.requester.resumption.request.headers.form.item

import assertk.Assert
import assertk.assertions.hasSameContentAs
import io.ktor.util.asStream
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.Input
import io.ktor.utils.io.jvm.javaio.toInputStream

/**
 * Asserts that the bytes remaining to be read from the actual [ByteReadChannel] are equal to those
 * of the given one.
 *
 * Both actual and expected [ByteReadChannel]s are canceled after the assertion.
 *
 * @param T [ByteReadChannel] on which the assertion is being performed.
 * @param expected [ByteReadChannel] whose remaining bytes will be compared to the actual one.
 * @see ByteReadChannel.cancel
 */
internal fun <T : ByteReadChannel> Assert<T>.hasSameContentAs(
  expected: ByteReadChannel
): Assert<T> {
  transform(transform = ByteReadChannel::toInputStream).hasSameContentAs(expected.toInputStream())
  return this
}

/**
 * Asserts that the bytes to be read from the actual [Input] are equal to those of the given one.
 *
 * Both actual and expected [Input]s are closed after the assertion.
 *
 * @param T [Input] on which the assertion is being performed.
 * @param expected [Input] to be compared to the actual one.
 * @see Input.close
 */
internal fun <T : Input> Assert<T>.hasSameContentAs(expected: T): Assert<T> {
  transform(transform = Input::asStream).hasSameContentAs(expected.asStream())
  return this
}
