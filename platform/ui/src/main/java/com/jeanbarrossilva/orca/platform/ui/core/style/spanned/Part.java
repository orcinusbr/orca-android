package com.jeanbarrossilva.orca.platform.ui.core.style.spanned;

import androidx.annotation.NonNull;
import java.util.Objects;
import kotlin.ranges.IntRange;

/**
 * Portion of a {@link android.text.Spanned} that either has or doesn't have a span applied to it.
 */
class Part {
  /** Indices to which this {@link Part} refers. */
  @NonNull private final IntRange indices;

  /**
   * {@link Part} to which a span has been applied.
   *
   * @see Spanned#getSpan()
   */
  static class Spanned extends Part {
    /** Span that's been applied to the specified {@link Part#indices}. */
    @NonNull private final Object span;

    Spanned(@NonNull IntRange indices, @NonNull Object span) {
      super(indices);
      this.span = span;
    }

    @Override
    public boolean equals(Object other) {
      return other instanceof Spanned
          && getIndices().equals(((Spanned) other).getIndices())
          && span == ((Spanned) other).span;
    }

    @Override
    public int hashCode() {
      return Objects.hash(getIndices(), span);
    }

    @NonNull
    @Override
    public String toString() {
      return "Part.Spanned(indices=" + getIndices() + ", span=" + span + ')';
    }

    /** Gets the span that's been applied to the specified {@link Spanned#indices}. */
    @NonNull
    Object getSpan() {
      return span;
    }
  }

  Part(@NonNull IntRange indices) {
    this.indices = indices;
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof Part && indices.equals(((Part) other).indices);
  }

  @Override
  public int hashCode() {
    return Objects.hash(indices);
  }

  @NonNull
  @Override
  public String toString() {
    return "Part(indices=" + indices + ')';
  }

  /** Gets the indices to which this {@link Part} refers. */
  @NonNull
  IntRange getIndices() {
    return indices;
  }
}
