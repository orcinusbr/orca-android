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

@file:JvmName("Pipelines")

package br.com.orcinus.orca.core.mastodon.notification.service.async

import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.concurrent.Executor
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.Future
import java.util.function.BiConsumer
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Supplier

/**
 * [Pipeline] that delegates its behaviors to the [delegate].
 *
 * @param delegate [CompletableFuture] on which this class is based.
 */
@JvmInline
private value class DelegatorPipeline<T>(private val delegate: CompletableFuture<T>) :
  Pipeline<T>, Future<T> by delegate, CompletionStage<T> by delegate {
  override fun <U : Any?> thenApply(fn: Function<in T, out U>?): Pipeline<U> {
    return delegate.thenApply(fn).toPipeline()
  }

  override fun <U : Any?> thenApplyAsync(fn: Function<in T, out U>?): Pipeline<U> {
    return delegate.thenApplyAsync(fn).toPipeline()
  }

  override fun <U : Any?> thenApplyAsync(
    fn: Function<in T, out U>?,
    executor: Executor?
  ): Pipeline<U> {
    return delegate.thenApplyAsync(fn, executor).toPipeline()
  }

  override fun thenAccept(action: Consumer<in T>?): Pipeline<Void> {
    return delegate.thenAccept(action).toPipeline()
  }

  override fun thenAcceptAsync(action: Consumer<in T>?): Pipeline<Void> {
    return delegate.thenAcceptAsync(action).toPipeline()
  }

  override fun thenAcceptAsync(action: Consumer<in T>?, executor: Executor?): Pipeline<Void> {
    return delegate.thenAcceptAsync(action, executor).toPipeline()
  }

  override fun thenRun(action: Runnable?): Pipeline<Void> {
    return delegate.thenRun(action).toPipeline()
  }

  override fun thenRunAsync(action: Runnable?): Pipeline<Void> {
    return delegate.thenRunAsync(action).toPipeline()
  }

  override fun thenRunAsync(action: Runnable?, executor: Executor?): Pipeline<Void> {
    return delegate.thenRunAsync(action, executor).toPipeline()
  }

  override fun <U : Any?, V : Any?> thenCombine(
    other: CompletionStage<out U>?,
    fn: BiFunction<in T, in U, out V>?
  ): Pipeline<V> {
    return delegate.thenCombine(other, fn).toPipeline()
  }

  override fun <U : Any?, V : Any?> thenCombineAsync(
    other: CompletionStage<out U>?,
    fn: BiFunction<in T, in U, out V>?
  ): Pipeline<V> {
    return delegate.thenCombineAsync(other, fn).toPipeline()
  }

  override fun <U : Any?, V : Any?> thenCombineAsync(
    other: CompletionStage<out U>?,
    fn: BiFunction<in T, in U, out V>?,
    executor: Executor?
  ): Pipeline<V> {
    return delegate.thenCombineAsync(other, fn).toPipeline()
  }

  override fun <U : Any?> thenAcceptBoth(
    other: CompletionStage<out U>?,
    action: BiConsumer<in T, in U>?
  ): Pipeline<Void> {
    return delegate.thenAcceptBoth(other, action).toPipeline()
  }

  override fun <U : Any?> thenAcceptBothAsync(
    other: CompletionStage<out U>?,
    action: BiConsumer<in T, in U>?
  ): Pipeline<Void> {
    return delegate.thenAcceptBothAsync(other, action).toPipeline()
  }

  override fun <U : Any?> thenAcceptBothAsync(
    other: CompletionStage<out U>?,
    action: BiConsumer<in T, in U>?,
    executor: Executor?
  ): Pipeline<Void> {
    return delegate.thenAcceptBothAsync(other, action).toPipeline()
  }

  override fun runAfterBoth(other: CompletionStage<*>?, action: Runnable?): Pipeline<Void> {
    return delegate.runAfterBoth(other, action).toPipeline()
  }

  override fun runAfterBothAsync(other: CompletionStage<*>?, action: Runnable?): Pipeline<Void> {
    return delegate.runAfterBothAsync(other, action).toPipeline()
  }

  override fun runAfterBothAsync(
    other: CompletionStage<*>?,
    action: Runnable?,
    executor: Executor?
  ): Pipeline<Void> {
    return delegate.runAfterBothAsync(other, action, executor).toPipeline()
  }

  override fun <U : Any?> applyToEither(
    other: CompletionStage<out T>?,
    fn: Function<in T, U>?
  ): Pipeline<U> {
    return delegate.applyToEither(other, fn).toPipeline()
  }

  override fun <U : Any?> applyToEitherAsync(
    other: CompletionStage<out T>?,
    fn: Function<in T, U>?
  ): Pipeline<U> {
    return delegate.applyToEitherAsync(other, fn).toPipeline()
  }

  override fun <U : Any?> applyToEitherAsync(
    other: CompletionStage<out T>?,
    fn: Function<in T, U>?,
    executor: Executor?
  ): Pipeline<U> {
    return delegate.applyToEitherAsync(other, fn, executor).toPipeline()
  }

  override fun acceptEither(
    other: CompletionStage<out T>?,
    action: Consumer<in T>?
  ): Pipeline<Void> {
    return delegate.acceptEither(other, action).toPipeline()
  }

  override fun acceptEitherAsync(
    other: CompletionStage<out T>?,
    action: Consumer<in T>?
  ): Pipeline<Void> {
    return delegate.acceptEitherAsync(other, action).toPipeline()
  }

  override fun acceptEitherAsync(
    other: CompletionStage<out T>?,
    action: Consumer<in T>?,
    executor: Executor?
  ): Pipeline<Void> {
    return delegate.acceptEitherAsync(other, action, executor).toPipeline()
  }

  override fun runAfterEither(other: CompletionStage<*>?, action: Runnable?): Pipeline<Void> {
    return delegate.runAfterEither(other, action).toPipeline()
  }

  override fun runAfterEitherAsync(other: CompletionStage<*>?, action: Runnable?): Pipeline<Void> {
    return delegate.runAfterEitherAsync(other, action).toPipeline()
  }

  override fun runAfterEitherAsync(
    other: CompletionStage<*>?,
    action: Runnable?,
    executor: Executor?
  ): Pipeline<Void> {
    return delegate.runAfterEitherAsync(other, action, executor).toPipeline()
  }

  override fun <U : Any?> thenCompose(fn: Function<in T, out CompletionStage<U>>?): Pipeline<U> {
    return delegate.thenCompose(fn).toPipeline()
  }

  override fun <U : Any?> thenComposeAsync(
    fn: Function<in T, out CompletionStage<U>>?
  ): Pipeline<U> {
    return delegate.thenComposeAsync(fn).toPipeline()
  }

  override fun <U : Any?> thenComposeAsync(
    fn: Function<in T, out CompletionStage<U>>?,
    executor: Executor?
  ): Pipeline<U> {
    return delegate.thenComposeAsync(fn, executor).toPipeline()
  }

  override fun <U : Any?> handle(fn: BiFunction<in T, Throwable, out U>?): Pipeline<U> {
    return delegate.handle(fn).toPipeline()
  }

  override fun <U : Any?> handleAsync(fn: BiFunction<in T, Throwable, out U>?): Pipeline<U> {
    return delegate.handleAsync(fn).toPipeline()
  }

  override fun <U : Any?> handleAsync(
    fn: BiFunction<in T, Throwable, out U>?,
    executor: Executor?
  ): Pipeline<U> {
    return delegate.handleAsync(fn, executor).toPipeline()
  }

  override fun whenComplete(action: BiConsumer<in T, in Throwable>?): Pipeline<T> {
    return delegate.whenComplete(action).toPipeline()
  }

  override fun whenCompleteAsync(action: BiConsumer<in T, in Throwable>?): Pipeline<T> {
    return delegate.whenCompleteAsync(action).toPipeline()
  }

  override fun whenCompleteAsync(
    action: BiConsumer<in T, in Throwable>?,
    executor: Executor?
  ): Pipeline<T> {
    return delegate.whenCompleteAsync(action, executor).toPipeline()
  }

  override fun exceptionally(fn: Function<Throwable, out T>?): Pipeline<T> {
    return delegate.exceptionally(fn).toPipeline()
  }
}

/**
 * Intersection between a [Future] and a [CompletionStage] with explicit one-shot computation,
 * allowing for both compositions atop and retrieval of a single value.
 *
 * Differs from a [CompletableFuture] in that it cannot be completed, given that it merely aims to
 * provide read-only access to the value being computed and the creation of other [Pipeline]s from
 * it.
 *
 * @param T Value to be retrieved.
 */
internal interface Pipeline<T> : Future<T>, CompletionStage<T> {
  override fun <U : Any?> thenApply(fn: Function<in T, out U>?): Pipeline<U>

  override fun <U : Any?> thenApplyAsync(fn: Function<in T, out U>?): Pipeline<U>

  override fun <U : Any?> thenApplyAsync(
    fn: Function<in T, out U>?,
    executor: Executor?
  ): Pipeline<U>

  override fun thenAccept(action: Consumer<in T>?): Pipeline<Void>

  override fun thenAcceptAsync(action: Consumer<in T>?): Pipeline<Void>

  override fun thenAcceptAsync(action: Consumer<in T>?, executor: Executor?): Pipeline<Void>

  override fun thenRun(action: Runnable?): Pipeline<Void>

  override fun thenRunAsync(action: Runnable?): Pipeline<Void>

  override fun thenRunAsync(action: Runnable?, executor: Executor?): Pipeline<Void>

  override fun <U : Any?, V : Any?> thenCombine(
    other: CompletionStage<out U>?,
    fn: BiFunction<in T, in U, out V>?
  ): Pipeline<V>

  override fun <U : Any?, V : Any?> thenCombineAsync(
    other: CompletionStage<out U>?,
    fn: BiFunction<in T, in U, out V>?
  ): Pipeline<V>

  override fun <U : Any?, V : Any?> thenCombineAsync(
    other: CompletionStage<out U>?,
    fn: BiFunction<in T, in U, out V>?,
    executor: Executor?
  ): Pipeline<V>

  override fun <U : Any?> thenAcceptBoth(
    other: CompletionStage<out U>?,
    action: BiConsumer<in T, in U>?
  ): Pipeline<Void>

  override fun <U : Any?> thenAcceptBothAsync(
    other: CompletionStage<out U>?,
    action: BiConsumer<in T, in U>?
  ): Pipeline<Void>

  override fun <U : Any?> thenAcceptBothAsync(
    other: CompletionStage<out U>?,
    action: BiConsumer<in T, in U>?,
    executor: Executor?
  ): Pipeline<Void>

  override fun runAfterBoth(other: CompletionStage<*>?, action: Runnable?): Pipeline<Void>

  override fun runAfterBothAsync(other: CompletionStage<*>?, action: Runnable?): Pipeline<Void>

  override fun runAfterBothAsync(
    other: CompletionStage<*>?,
    action: Runnable?,
    executor: Executor?
  ): Pipeline<Void>

  override fun <U : Any?> applyToEither(
    other: CompletionStage<out T>?,
    fn: Function<in T, U>?
  ): Pipeline<U>

  override fun <U : Any?> applyToEitherAsync(
    other: CompletionStage<out T>?,
    fn: Function<in T, U>?
  ): Pipeline<U>

  override fun <U : Any?> applyToEitherAsync(
    other: CompletionStage<out T>?,
    fn: Function<in T, U>?,
    executor: Executor?
  ): Pipeline<U>

  override fun acceptEither(other: CompletionStage<out T>?, action: Consumer<in T>?): Pipeline<Void>

  override fun acceptEitherAsync(
    other: CompletionStage<out T>?,
    action: Consumer<in T>?
  ): Pipeline<Void>

  override fun acceptEitherAsync(
    other: CompletionStage<out T>?,
    action: Consumer<in T>?,
    executor: Executor?
  ): Pipeline<Void>

  override fun runAfterEither(other: CompletionStage<*>?, action: Runnable?): Pipeline<Void>

  override fun runAfterEitherAsync(other: CompletionStage<*>?, action: Runnable?): Pipeline<Void>

  override fun runAfterEitherAsync(
    other: CompletionStage<*>?,
    action: Runnable?,
    executor: Executor?
  ): Pipeline<Void>

  override fun <U : Any?> thenCompose(fn: Function<in T, out CompletionStage<U>>?): Pipeline<U>

  override fun <U : Any?> thenComposeAsync(fn: Function<in T, out CompletionStage<U>>?): Pipeline<U>

  override fun <U : Any?> thenComposeAsync(
    fn: Function<in T, out CompletionStage<U>>?,
    executor: Executor?
  ): Pipeline<U>

  override fun <U : Any?> handle(fn: BiFunction<in T, Throwable, out U>?): Pipeline<U>

  override fun <U : Any?> handleAsync(fn: BiFunction<in T, Throwable, out U>?): Pipeline<U>

  override fun <U : Any?> handleAsync(
    fn: BiFunction<in T, Throwable, out U>?,
    executor: Executor?
  ): Pipeline<U>

  override fun whenComplete(action: BiConsumer<in T, in Throwable>?): Pipeline<T>

  override fun whenCompleteAsync(action: BiConsumer<in T, in Throwable>?): Pipeline<T>

  override fun whenCompleteAsync(
    action: BiConsumer<in T, in Throwable>?,
    executor: Executor?
  ): Pipeline<T>

  override fun exceptionally(fn: Function<Throwable, out T>?): Pipeline<T>
}

/**
 * Creates a [Pipeline].
 *
 * ###### Implementation notes
 *
 * This returns a new instance of a [Pipeline] which simply forwards calls to its methods to an
 * underlying [CompletableFuture]. Best-case scenario, there will be an overhead of 40 bytes for the
 * sole purpose of _not_ exposing a set of completion methods:
 *
 * | Instance                         | Metadata                                 | Size in bytes |
 * |----------------------------------|------------------------------------------|---------------|
 * | [CompletableFuture]              | Header                                   | 8             |
 * |                                  | [Object] `result`                        | 4             |
 * |                                  | `CompletableFuture.Continuation` `stack` | 4             |
 * | [DelegatorPipeline]              | Header                                   | 8             |
 * |                                  | [CompletableFuture] `delegate`           | 4             |
 * | `CompletableFuture.Continuation` | Header                                   | 8             |
 * |                                  | `CompletableFuture.Continuation` `next`  | 4             |
 *
 * Although such footprint is mostly irrelevant, is it necessary?
 *
 * @param value Object to be immediately produced.
 */
@JvmName("immediate")
internal fun <T> pipelineOf(value: T): Pipeline<T> {
  val delegate = CompletableFuture.completedFuture(value)
  return DelegatorPipeline(delegate)
}

/**
 * Creates a [Pipeline].
 *
 * ###### Implementation notes
 *
 * This returns a new instance of a [Pipeline] which simply forwards calls to its methods to an
 * underlying [CompletableFuture]. Best-case scenario, there will be an overhead of 56 bytes for the
 * sole purpose of _not_ exposing a set of completion methods:
 *
 * | Instance                          | Metadata                                 | Size in bytes |
 * |-----------------------------------|------------------------------------------|---------------|
 * | `CompletableFuture.AsyncSupplier` | Header                                   | 8             |
 * |                                   | [CompletableFuture] `dep`                | 4             |
 * |                                   | [Supplier] `fn`                          | 4             |
 * | [CompletableFuture]               | Header                                   | 8             |
 * |                                   | [Object] `result`                        | 4             |
 * |                                   | `CompletableFuture.Continuation` `stack` | 4             |
 * | [DelegatorPipeline]               | Header                                   | 8             |
 * |                                   | [CompletableFuture] `delegate`           | 4             |
 * | `CompletableFuture.Continuation`  | Header                                   | 8             |
 * |                                   | `CompletableFuture.Continuation` `next`  | 4             |
 *
 * Although such footprint is mostly irrelevant, is it necessary?
 *
 * @param compute Computes the value produced by the returned [Pipeline]. This lambda is executed in
 *   the [ForkJoinPool.commonPool] if parallelism is supported; otherwise, it is run in a standalone
 *   [Thread].
 * @see ForkJoinPool.execute
 */
@JvmName("async")
internal fun <T> pipeline(compute: () -> T): Pipeline<T> {
  return DelegatorPipeline(CompletableFuture.supplyAsync(compute))
}

/**
 * Converts this [CompletionStage] into a [Pipeline].
 *
 * @param T Value to be retrieved.
 */
private fun <T> CompletionStage<T>.toPipeline(): Pipeline<T> {
  return when (this) {
    is Pipeline<T> -> this
    is CompletableFuture<T> -> DelegatorPipeline(this)
    else -> toCompletableFuture().toPipeline()
  }
}
