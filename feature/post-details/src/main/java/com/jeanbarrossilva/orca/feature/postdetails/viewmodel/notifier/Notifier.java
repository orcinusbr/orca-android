package com.jeanbarrossilva.orca.feature.postdetails.viewmodel.notifier;

import androidx.annotation.NonNull;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.MutableStateFlow;

/**
 * No-op structure that serves the sole purpose of being emitted to a {@link
 * MutableStateFlowExtensions#notifierFlow()}, allowing it to send notifications for other {@link
 * Flow}s that collect it to emit new values.
 *
 * @see Notifier#initial
 * @see Notifier#subsequent()
 * @see Notifier#after
 * @see MutableStateFlowExtensions#notify(MutableStateFlow)
 */
public final class Notifier {
  /** Lazily instantiated {@link Notifier} returned by {@link #subsequent()}. */
  private static Notifier subsequent;

  /** Name by which this {@link Notifier} can be identified. */
  private final String name;

  /**
   * {@link Notifier} to be first emitted to a {@link MutableStateFlowExtensions#notifierFlow()}.
   */
  static Notifier initial = new Notifier("Notifier.initial");

  /** {@link Notifier} to be emitted after the {@link #initial} one. */
  static Notifier subsequent() {
    if (subsequent == null) {
      subsequent = new Notifier("Notifier.subsequent");
    }
    return subsequent;
  }

  /**
   * Gets the {@link Notifier} that succeeds the given one.
   *
   * @param notifier {@link Notifier} whose successor will be obtained.
   * @throws IllegalArgumentException If the given {@link Notifier} is neither the {@link #initial}
   *     nor the {@link #subsequent} one.
   */
  static Notifier after(Notifier notifier) throws IllegalArgumentException {
    final Notifier successor;
    if (notifier == initial) {
      successor = subsequent();
    } else if (notifier == subsequent()) {
      successor = initial;
    } else {
      throw new IllegalArgumentException(
          "Notifier should be either the initial or the subsequent one.");
    }
    return successor;
  }

  private Notifier(String name) {
    this.name = name;
  }

  @NonNull
  @Override
  public String toString() {
    return name;
  }
}
