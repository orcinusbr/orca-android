/*
 * Copyright © 2025 Orcinus
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

@file:JvmName("Maybes")

package br.com.orcinus.orca.std.func.monad

/**
 * Class private to the machinery of [Maybe] that allows for distinguishing between a successful
 * value (whose type is whichever _but_ this one) and a failed value (whose type is _exclusively_
 * this one).
 *
 * @property exception [Exception] resulted from trying to obtain a successful value.
 */
@JvmInline private value class Failure(val exception: Exception)

/**
 * Monad that holds either a successful value or an [Exception] because of which such value failed
 * to be obtained. Similar to a Kotlin [Result], but different in that it specifies the failure that
 * may occur.
 *
 * @param E [Exception] because of which the value cannot be obtained.
 * @param V The successful value.
 * @property value Either a [V] (when successful) or a [Failure] by which the [Exception] is wrapped
 *   (when failed).
 */
@JvmInline
value class Maybe<out E : Exception, out V>
private constructor(@PublishedApi internal val value: Any?) {
  /** Whether the value has been obtained successfully. */
  val isSuccessful
    get() = !isFailed

  /** Whether obtaining the value has resulted in a failure. */
  val isFailed
    get() = value is Failure

  override fun toString() =
    if (isSuccessful) {
      "Maybe.successful($value)"
    } else {
      "Maybe.failed(${(value as Failure).exception})"
    }

  /**
   * Erases information about the successful value held by this [Maybe] by converting it into a
   * `Maybe<E, Unit>`. Intended for scenarios in which an internal context should not be leaked to
   * consumers, functionally indicating to them only that obtaining the value _may_ throw.
   */
  @Suppress("UNCHECKED_CAST") fun unit() = if (value is Unit) this as Maybe<E, Unit> else map {}

  /**
   * Produces a [Maybe] whose value is the result of applying the given transformation to that of
   * this one.
   *
   * @param T Result of the transformation.
   * @param transform Transformation to be performed on the successful value of this [Maybe].
   */
  @Suppress("UNCHECKED_CAST")
  inline fun <T> map(transform: (V) -> T) =
    if (isSuccessful) successful(transform(value as V)) else this as Maybe<E, T>

  /** Obtains the successful value or returns `null` in case this is failed. */
  fun getValueOrNull() = (this as Maybe<*, V?>).getValueOrDefault(null)

  /** Obtains the successful value or throws in case this [Maybe] holds a failure. */
  @Suppress("UNCHECKED_CAST")
  @Throws
  fun getValueOrThrow() = if (isSuccessful) value as V else throw (value as Failure).exception

  /**
   * Obtains the [Exception] because of which obtaining the value has failed or `null` in case this
   * is successful.
   */
  fun getExceptionOrNull() = (value as? Failure)?.exception

  /**
   * Performs the given [action] on the value in case this is successful.
   *
   * @param action Operation to be executed with the successful value of this [Maybe].
   */
  inline fun onSuccessful(action: (V) -> Unit) = apply {
    if (isSuccessful) {
      @Suppress("UNCHECKED_CAST") action(value as V)
    }
  }

  /**
   * Obtains the successful value or returns the given default one in case this is failed.
   *
   * @param defaultValue Value returned by default if this [Maybe] holds a failure.
   */
  @Suppress("UNCHECKED_CAST")
  internal fun getValueOrDefault(defaultValue: @UnsafeVariance V) =
    if (isSuccessful) value as V else defaultValue

  companion object {
    override fun toString() = "Maybe.Companion"

    /**
     * Produces a [Maybe] that holds a successful [Unit], for contexts in which any failures are
     * expected.
     */
    @JvmStatic fun successful(): Maybe<*, Unit> = successful<Exception, _>(Unit)

    /**
     * Produces a [Maybe] that holds a successful value.
     *
     * @param E [Exception] because of which the value cannot be obtained.
     * @param V The successful value.
     * @param value Successful value to be held.
     */
    @JvmStatic fun <E : Exception, V> successful(value: V) = Maybe<E, V>(value)

    /**
     * Produces a [Maybe] that holds a failure.
     *
     * @param E Type of the [exception].
     * @param V The successful value.
     * @param exception [Exception] because of which the value cannot be obtained.
     */
    @JvmStatic fun <E : Exception, V> failed(exception: E) = Maybe<E, V>(Failure(exception))
  }
}

/**
 * Transforms each element in the [Iterable] of this [Maybe] and combines the produced [Maybe]s into
 * a single one. That which is returned by this method holds either all successful transformations
 * or the [Exception] thrown by the first failing one.
 *
 * @param E [Exception] because of which the value cannot be obtained.
 * @param V Value resulted from this [Maybe].
 * @param T Value resulted from a transformation.
 * @param transform Transforms an element of the value of the receiver [Maybe] into another [Maybe].
 */
inline fun <reified E : Exception, V, T> Maybe<E, Iterable<V>>.onEach(
  transform: (V) -> Maybe<E, T>
) = flatMap { elements ->
  try {
    Maybe.successful(elements.map { element -> transform(element).getValueOrThrow() })
  } catch (exception: Exception) {
    Maybe.failed(exception as E)
  }
}

/**
 * Returns the [Maybe] resulted from the transformation in case it is not a failure; otherwise, the
 * receiver one is cast to `Maybe<E, R>` and returned (which is safe because its successful value is
 * nonexistent and, thus, unobtainable).
 *
 * @param E [Exception] because of which the value cannot be obtained.
 * @param V Value resulted from this [Maybe].
 * @param T Value resulted from a transformation.
 * @param transform Transforms the value of the receiver [Maybe] into another [Maybe].
 */
inline fun <E : Exception, V, T> Maybe<E, V>.flatMap(transform: (V) -> Maybe<E, T>) =
  map(transform).flatten()

/**
 * Returns the [Maybe] resulted from this one in case this one is not a failure; otherwise, this one
 * is cast to `Maybe<E, R>` and returned (which is safe because its successful value is nonexistent
 * and, thus, unobtainable).
 *
 * @param E [Exception] because of which the value cannot be obtained.
 * @param V Value resulted from the nested [Maybe].
 */
@Suppress("UNCHECKED_CAST")
fun <E : Exception, V> Maybe<E, Maybe<E, V>>.flatten() = getValueOrDefault(this as Maybe<E, V>)
