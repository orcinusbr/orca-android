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

package br.com.orcinus.orca.core.mastodon.network.requester.request.headers.input

import io.ktor.utils.io.bits.Memory
import io.ktor.utils.io.bits.fill
import io.ktor.utils.io.bits.get
import io.ktor.utils.io.core.Input
import io.ktor.utils.io.core.internal.ChunkBuffer
import io.ktor.utils.io.pool.ObjectPool

/**
 * [Input] that delegates [Memory]-filling behavior to the destination itself and releases the
 * [head] when the source is requested to be closed.
 *
 * @see fill
 * @see ChunkBuffer.release
 * @see closeSource
 */
internal class DefaultInput(
  private val head: ChunkBuffer = ChunkBuffer.Empty,
  remaining: Long = head.totalReadRemaining,
  pool: ObjectPool<ChunkBuffer> = ChunkBuffer.Pool
) : Input(head, remaining, pool) {
  override fun fill(destination: Memory, offset: Int, length: Int): Int {
    val count = length - offset
    destination.fill(offset, count, destination[count])
    return count
  }

  override fun closeSource() {
    head.release(pool)
  }
}
