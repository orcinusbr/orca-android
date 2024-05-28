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

package br.com.orcinus.orca.core.mastodon.network.request.headers.pool

import br.com.orcinus.orca.core.mastodon.network.InternalNetworkApi
import io.ktor.utils.io.pool.DefaultPool
import io.ktor.utils.io.pool.ObjectPool

/**
 * [ObjectPool] that transforms object instances from an original [ObjectPool].
 *
 * @property originalPool [ObjectPool] to be transformed.
 * @property transform Modification to be performed onto an object of the [originalPool].
 * @see transform
 * @see originalPool
 */
private class TransformerObjectPool<I : Any, O : Any>(
  private val originalPool: ObjectPool<I>,
  private val transform: (I) -> O
) : ObjectPool<O> {
  /**
   * Transformations that have been performed after this [TransformerObjectPool] has been either
   * created or last disposed.
   *
   * @see dispose
   */
  private val history = hashMapOf<O, I>()

  override val capacity = originalPool.capacity

  override fun borrow(): O {
    val originalInstance = originalPool.borrow()
    val transformedInstance = transform(originalInstance)
    history[transformedInstance] = originalInstance
    return transformedInstance
  }

  override fun dispose() {
    originalPool.dispose()
    history.clear()
  }

  override fun recycle(instance: O) {
    val originalInstance = history.getValue(instance)
    originalPool.recycle(originalInstance)
    history.remove(instance)
  }
}

/**
 * Creates an [ObjectPool].
 *
 * @param capacity Amount of objects that can be pooled.
 * @param production Creates an instance of an object to be pooled.
 */
@InternalNetworkApi
internal fun <T : Any> objectPoolOf(capacity: Int = 1, production: () -> T): ObjectPool<T> {
  return object : DefaultPool<T>(capacity) {
    override fun produceInstance(): T {
      return production()
    }
  }
}

/**
 * Produces an [ObjectPool] whose object instances are a result of applying the given transformation
 * to those of the receiver one.
 *
 * @param transform Modification to be performed onto an object of this [ObjectPool].
 */
@InternalNetworkApi
internal fun <I : Any, O : Any> ObjectPool<I>.map(transform: (I) -> O): ObjectPool<O> {
  return TransformerObjectPool(this, transform)
}
