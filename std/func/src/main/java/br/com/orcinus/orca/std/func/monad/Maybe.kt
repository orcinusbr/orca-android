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
 * Class internal to the machinery of [Maybe] which allows for distinguishing between a successful
 * value (whose type is whichever _but_ that of this class) and a failed value (whose type is
 * _exclusively_ that of this class).
 *
 * @property exception [Exception] resulted from trying to obtain a successful value.
 */
@JvmInline @PublishedApi internal value class Failure(val exception: Exception)

/**
 * [IllegalStateException] thrown if a transformation of [join] throws an [Exception] different from
 * that specified by the receiver [Maybe].
 *
 * @param exception The unexpected [Exception].
 * @param element Element in the successful [Iterable] of the receiver [Maybe] whose transformation
 *   caused the unexpected failure.
 */
class UnexpectedFailureException
@PublishedApi
internal constructor(exception: Exception, element: Any?) :
  IllegalStateException(
    "A non-${exception::class.simpleName} ($exception) was thrown while transforming $element."
  )

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
class Maybe<out E : Exception, out V> private constructor(@PublishedApi internal val value: Any?) {
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
   * Produces a [Maybe] whose [Exception] is the result of applying the given transformation to that
   * of this one. Essentially, acts as a [map] for failures instead of successful values.
   *
   * @param T Result of the transformation.
   * @param transform Transformation to be performed on the [Exception] of this [Maybe].
   */
  @Suppress("UNCHECKED_CAST")
  inline fun <T : Exception> failWith(transform: (E) -> T) =
    if (isFailed) failed(transform((value as Failure).exception as E)) else this as Maybe<T, V>

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
 * Produces a [Maybe] from the successful value of this one and that which has been given.
 *
 * @param E [Exception] because of which the value cannot be obtained.
 * @param V Value resulted from this [Maybe].
 * @param T Successful value of the given [Maybe].
 * @param R Successful, combined value.
 * @param other [Maybe] whose value is transformed into another alongside that of this one.
 * @param transform Transformation to be applied to both successful values.
 */
inline fun <E : Exception, V, T, R> Maybe<E, V>.combine(
  other: Maybe<E, T>,
  transform: (V, T) -> R
) = flatMap { thisValue -> other.map { otherValue -> transform(thisValue, otherValue) } }

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
@Deprecated(
  "\"onEach\" is misleading: the standard Kotlin library uses this name for collection methods " +
    "which perform a unit operation on each element and, when finished, return the collection " +
    "itself. On the other hand, this method transforms each element of a successful iterable " +
    "while requiring that such transformation produces a specific result — another maybe — and " +
    "joins them, producing a single one (similar to joinTo).",
  ReplaceWith("join(transform)", imports = ["br.com.orcinus.orca.std.func.monad.join"])
)
inline fun <reified E : Exception, V, T> Maybe<E, Iterable<V>>.onEach(
  transform: (V) -> Maybe<E, T>
) = join(transform)

/**
 * Transforms each element in the [Iterable] of this [Maybe] and combines the produced [Maybe]s into
 * a single one. That which is returned by this method holds either all successful transformations
 * or the [Exception] thrown by the first failing one.
 *
 * @param E [Exception] because of which the value cannot be obtained.
 * @param V Value resulted from this [Maybe].
 * @param T Value resulted from a transformation.
 * @param transform Transforms an element of the value of the receiver [Maybe] into another [Maybe].
 * @throws UnexpectedFailureException If the transformation of an element throws an [Exception]
 *   other than [E].
 */
@Throws(UnexpectedFailureException::class)
inline fun <reified E : Exception, V, T> Maybe<E, Iterable<V>>.join(transform: (V) -> Maybe<E, T>) =
  flatMap { elements ->
    val transformations = ArrayList<T>(/* initialCapacity = */ elements.count())
    for (element in elements) {
      try {
        transformations.add(transform(element).getValueOrThrow())
      } catch (exception: Exception) {
        if (exception !is E) {
          throw UnexpectedFailureException(exception, element)
        }
        return@flatMap Maybe.failed(exception)
      }
    }
    Maybe.successful<_, List<T>>(transformations)
  }

/**
 * Returns the [Maybe] resulted from the transformation in case it is not a failure; otherwise, the
 * receiver one is cast to `Maybe<E, T>` and returned (which is safe because its successful value is
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
 * is cast to `Maybe<E, V>` and returned (which is safe because its successful value is nonexistent
 * and, thus, unobtainable).
 *
 * @param E [Exception] because of which the value cannot be obtained.
 * @param V Value resulted from the nested [Maybe].
 */
@Suppress("UNCHECKED_CAST")
fun <E : Exception, V> Maybe<E, Maybe<E, V>>.flatten() = getValueOrDefault(this as Maybe<E, V>)
