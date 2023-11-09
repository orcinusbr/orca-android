package com.jeanbarrossilva.orca.platform.ui.core.style.spanned;

import androidx.annotation.NonNull;
import java.util.List;
import java.util.Objects;
import kotlin.ranges.IntRange;

/**
 * Portion of a {@link android.text.Spanned} that either has or doesn't have a span applied to it.
 */
class Part {
  /** Indices to which this {@link Part} refers. */
  @NonNull private final IntRange indices;

  /**
   * {@link Part} to which spans have been applied.
   *
   * @see Spanned#getSpans()
   */
  static class Spanned extends Part {
    /** Spans that have been applied to the specified {@link Part#indices}. */
    @NonNull private final List<Object> spans;

    private Spanned(@NonNull IntRange indices, @NonNull List<Object> spans) {
      super(indices);
      this.spans = spans;
    }

    @Override
    public boolean equals(Object other) {
      return other instanceof Spanned
          && getIndices().equals(((Spanned) other).getIndices())
          && spans.equals(((Spanned) other).spans);
    }

    @Override
    public int hashCode() {
      return Objects.hash(getIndices(), spans);
    }

    @NonNull
    @Override
    public String toString() {
      return "Part.Spanned(indices=" + getIndices() + ", spans=" + spans + ')';
    }

    /** Gets the spans that have been applied to the specified {@link Spanned#indices}. */
    @NonNull
    Object getSpans() {
      return spans;
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

  /**
   * Creates a {@link Part.Spanned} with the given spans.
   *
   * @param spans Spans that have been applied.
   */
  Spanned span(Object... spans) {
    List<Object> spansAsList = List.of(spans);
    return new Spanned(indices, spansAsList);
  }
}
