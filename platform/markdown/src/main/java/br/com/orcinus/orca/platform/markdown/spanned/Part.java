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

package br.com.orcinus.orca.platform.markdown.spanned;

import android.content.Context;
import android.text.ParcelableSpan;
import androidx.annotation.NonNull;
import br.com.orcinus.orca.platform.markdown.spanned.span.AnyExtensions;
import java.util.List;
import java.util.Objects;
import kotlin.ranges.IntRange;

/**
 * Portion of a {@link android.text.Spanned} that either has or doesn't have a {@link
 * ParcelableSpan} applied to it.
 */
public class Part {
  /** Indices to which this {@link Part} refers. */
  @NonNull private final IntRange indices;

  /**
   * {@link Part} to which spans have been applied.
   *
   * @see Spanned#getSpans()
   */
  public static class Spanned extends Part {
    /**
     * {@link Context} with which two spans can be compared structurally.
     *
     * @see AnyExtensions#isStructurallyEqual(Object, Context, Object)
     */
    @NonNull private final Context context;

    /** Spans that have been applied to the specified {@link Part#indices}. */
    @NonNull private final List<Object> spans;

    /**
     * {@link Part} to which spans have been applied.
     *
     * @param context {@link Context} with which two spans can be compared structurally.
     * @param indices Indices to which this {@link Part} refers.
     * @param spans Spans that have been applied to the specified {@link Part#indices}.
     * @see AnyExtensions#isStructurallyEqual(Object, Context, Object)
     * @see Spanned#getSpans()
     */
    private Spanned(
        @NonNull Context context, @NonNull IntRange indices, @NonNull List<Object> spans) {
      super(indices);
      this.context = context;
      this.spans = spans;
    }

    @Override
    public boolean equals(Object other) {
      return other instanceof Spanned
          && getIndices().equals(((Spanned) other).getIndices())
          && areSpansStructurallyEqual(((Spanned) other).spans);
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
    public List<Object> getSpans() {
      return spans;
    }

    /**
     * Returns whether the given spans are structurally equal to the ones that belong to this {@link
     * Spanned}.
     *
     * @param others Spans to which those of this {@link Spanned} will be structurally compared.
     */
    private boolean areSpansStructurallyEqual(List<Object> others) {
      if (spans.size() != others.size()) {
        return false;
      }
      for (int index = 0; index < spans.size(); index++) {
        if (!AnyExtensions.isStructurallyEqual(spans.get(index), context, others.get(index))) {
          return false;
        }
      }
      return true;
    }
  }

  /**
   * Portion of a {@link android.text.Spanned} that either has or doesn't have a {@link
   * ParcelableSpan} applied to it.
   *
   * @param indices Indices to which this {@link Part} refers.
   */
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
  public IntRange getIndices() {
    return indices;
  }

  /**
   * Creates a {@link Part.Spanned} with the given spans.
   *
   * @param context {@link Context} with which two spans can be compared structurally.
   * @param spans Spans that have been applied.
   */
  Spanned span(Context context, Object... spans) {
    List<Object> spansAsList = List.of(spans);
    return new Spanned(context, indices, spansAsList);
  }
}
