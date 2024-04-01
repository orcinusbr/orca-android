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

package br.com.orcinus.orca.platform.ime.scope.animation.stage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.time.Duration;
import java.util.Objects;
import org.jetbrains.annotations.Contract;

/**
 * Phase in which an animation can be.
 *
 * @see Stage#idle()
 * @see Stage#prepared(Duration)
 * @see Stage#started(Duration)
 * @see Stage#ongoing(Duration)
 * @see Stage#ended()
 */
public class Stage {
  /** {@link Stage#id} of an {@link Stage#idle()} {@link Stage}. */
  private static final byte IDLE = 0;

  /** {@link Stage#id} of a {@link Stage#prepared(Duration)} {@link Stage}. */
  private static final byte PREPARED = 1;

  /** {@link Stage#id} of a {@link Stage#started(Duration)} {@link Stage}. */
  private static final byte STARTED = 2;

  /** {@link Stage#id} of an {@link Stage#ongoing(Duration)} {@link Stage}. */
  private static final byte ONGOING = 3;

  /** {@link Stage#id} of an {@link Stage#ended()} {@link Stage}. */
  private static final byte ENDED = 4;

  /** {@link Stage} to always be returned by {@link Stage#idle()}. */
  @NonNull private static final Stage idle = new Stage(IDLE, Duration.ZERO);

  /** {@link Stage} to always be returned by {@link Stage#ended()}. */
  @NonNull private static final Stage ended = new Stage(ENDED, Duration.ZERO);

  /** Gets a {@link Stage} for when an animation is not currently being run. */
  @NonNull
  public static Stage idle() {
    return idle;
  }

  /**
   * Gets a {@link Stage} for when an animation is preparing to start running but hasn't done so
   * yet.
   *
   * @param duration {@link Duration} of the animation.
   */
  @Contract(value = "_ -> new", pure = true)
  @NonNull
  public static Stage prepared(Duration duration) {
    return new Stage(PREPARED, duration);
  }

  /**
   * Creates a {@link Stage} for when an animation has been requested to begin running.
   *
   * @param duration {@link Duration} of the animation.
   */
  @Contract(value = "_ -> new", pure = true)
  @NonNull
  public static Stage started(Duration duration) {
    return new Stage(STARTED, duration);
  }

  /**
   * Creates a {@link Stage} for when an animation has been started and is currently being run.
   *
   * @param duration {@link Duration} of the animation.
   * @see Stage#started(Duration)
   */
  @Contract(value = "_ -> new", pure = true)
  @NonNull
  public static Stage ongoing(Duration duration) {
    return new Stage(ONGOING, duration);
  }

  /**
   * Gets a {@link Stage} for when an animation was ongoing and has now finished running.
   *
   * @see Stage#ongoing(Duration)
   */
  @NonNull
  public static Stage ended() {
    return ended;
  }

  /** Uniquely identifies this specific {@link Stage}. */
  private final byte id;

  /** {@link Duration} of the animation. */
  @NonNull final Duration duration;

  /**
   * Phase in which an animation can be.
   *
   * @param id Uniquely identifies this specific {@link Stage}.
   * @param duration {@link Duration} of the animation.
   */
  private Stage(byte id, @NonNull Duration duration) {
    this.id = id;
    this.duration = duration;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    return obj instanceof Stage && id == ((Stage) obj).id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @NonNull
  @Override
  public String toString() {
    return switch (id) {
      case IDLE -> "Stage(IDLE)";
      case PREPARED -> String.format("Stage(PREPARED, %s)", duration);
      case STARTED -> String.format("Stage(STARTED, %s)", duration);
      case ONGOING -> String.format("Stage(ONGOING, %s)", duration);
      case ENDED -> "Stage(ENDED)";
      default -> "Stage(UNKNOWN)";
    };
  }

  /** Gets whether this is the {@link Stage#idle()} {@link Stage}. */
  public boolean isIdle() {
    return id == IDLE;
  }

  /** Gets whether this is a {@link Stage#prepared(Duration)} {@link Stage}. */
  public boolean isPrepared() {
    return id == PREPARED;
  }

  /** Gets whether this is a {@link Stage#started(Duration)} {@link Stage}. */
  public boolean isStarted() {
    return id == STARTED;
  }

  /** Gets whether this is an {@link Stage#ongoing(Duration)} {@link Stage}. */
  public boolean isOngoing() {
    return id == ONGOING;
  }

  /** Gets whether this is the {@link Stage#ended()} {@link Stage}. */
  public boolean isEnded() {
    return id == ENDED;
  }

  /**
   * Combines the {@link Stage}s, creating one that is the same as the other one but with the summed
   * {@link Stage#duration} of both.
   *
   * <p><b>NOTE</b>: Doesn't do anything if the {@link Stage} is an {@link Stage#idle} or {@link
   * Stage#ended} one, given that they cannot have a non-zero {@link Stage#duration} and, because of
   * that, this method simply returns them back.
   *
   * @noinspection unused
   * @param other {@link Stage} to combine this one with.
   */
  @NonNull
  public Stage plus(@NonNull Stage other) {
    if (other.isPrepared()) {
      return prepared(duration.plus(other.duration));
    } else if (other.isStarted()) {
      return started(duration.plus(other.duration));
    } else if (other.isOngoing()) {
      return ongoing(duration.plus(other.duration));
    } else {
      return other;
    }
  }
}
