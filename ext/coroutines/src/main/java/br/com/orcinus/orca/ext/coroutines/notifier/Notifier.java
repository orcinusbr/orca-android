/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.ext.coroutines.notifier;

import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
import kotlinx.coroutines.flow.StateFlow;
import org.jetbrains.annotations.NotNull;

/**
 * No-op structure that serves the sole purpose of being emitted to a {@link
 * MutableStateFlowExtensions#notifierFlow()}, allowing it to send notifications for other {@link
 * Flow}s that collect it to emit new values.
 *
 * <p>A call to {@link #next()} returns either the {@link #initial} {@link Notifier} if this is the
 * {@link #subsequent} one or the {@link #subsequent} if this is the {@link #initial} one, allowing
 * for them to be emitted interchangeably, working around the constraint of {@link StateFlow}s that
 * prevents them from emitting equal values consecutively.
 *
 * @see FlowCollector#emit(Object, Continuation)
 */
public final class Notifier {
  /** {@link Notifier} to be emitted after the {@link #initial} one. */
  @NotNull private static final Notifier subsequent = new Notifier("Notifier.subsequent");

  /** Name by which this {@link Notifier} can be identified. */
  @NotNull private final String name;

  /**
   * {@link Notifier} to be first emitted to a {@link MutableStateFlowExtensions#notifierFlow()}.
   */
  @NotNull static final Notifier initial = new Notifier("Notifier.initial");

  private Notifier(@NotNull String name) {
    this.name = name;
  }

  /**
   * Gets the {@link Notifier} that succeeds this one.
   *
   * @throws IllegalArgumentException If this {@link Notifier} is neither the {@link #initial} nor
   *     the {@link #subsequent} one.
   */
  @NotNull
  Notifier next() {
    final Notifier next;
    if (this == initial) {
      next = subsequent;
    } else if (this == subsequent) {
      next = initial;
    } else {
      throw new IllegalArgumentException(
          "Notifier should be either the initial or the subsequent one.");
    }
    return next;
  }

  @NotNull
  @Override
  public String toString() {
    return name;
  }
}
