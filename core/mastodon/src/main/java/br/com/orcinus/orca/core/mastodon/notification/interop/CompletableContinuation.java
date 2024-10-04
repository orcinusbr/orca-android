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

package br.com.orcinus.orca.core.mastodon.notification.interop;

import androidx.annotation.NonNull;
import br.com.orcinus.orca.core.mastodon.InternalMastodonApi;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;

/**
 * A {@link CompletionStage}-based {@link Continuation} for calling Kotlin suspending methods from
 * Java code.
 *
 * @param <T> Value with which this {@link Continuation} can be resumed.
 */
@InternalMastodonApi
public class CompletableContinuation<T> implements Continuation<T> {
  /**
   * {@link CompletableFuture} whose completion will be triggered when this {@link Continuation} is
   * resumed with a value.
   */
  @NonNull private final CompletableFuture<T> completableFuture = new CompletableFuture<>();

  /**
   * A {@link CompletionStage}-based {@link Continuation} for calling Kotlin suspending methods from
   * Java code.
   */
  public CompletableContinuation() {}

  @Override
  public void resumeWith(@NonNull Object o) {
    if (o instanceof Result.Failure) {
      completableFuture.completeExceptionally(((Result.Failure) o).exception);
    } else {
      //noinspection unchecked
      completableFuture.complete((T) o);
    }
  }

  @NonNull
  @Override
  public CoroutineContext getContext() {
    return EmptyCoroutineContext.INSTANCE;
  }

  /** Obtains the {@link CompletionStage} of this {@link Continuation}. */
  public CompletionStage<T> getCompletionStage() {
    return completableFuture;
  }
}
