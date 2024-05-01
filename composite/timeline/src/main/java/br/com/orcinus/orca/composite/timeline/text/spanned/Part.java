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

package br.com.orcinus.orca.composite.timeline.text.spanned;

import android.text.ParcelableSpan;
import androidx.annotation.NonNull;
import br.com.orcinus.orca.platform.markdown.span.ParcelableSpanExtensions;
import br.com.orcinus.orca.std.markdown.style.Style;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kotlin.ranges.IntRange;

/**
 * Portion of a {@link android.text.Spanned} that either has or doesn't have a {@link
 * ParcelableSpan} applied to it.
 */
class Part {
  /** Indices to which this {@link Part} refers. */
  @NonNull private final IntRange indices;

  /**
   * {@link Part} to which {@link ParcelableSpan}s have been applied.
   *
   * @see Spanned#getSpans()
   */
  static class Spanned extends Part {
    /** {@link ParcelableSpan}s that have been applied to the specified {@link Part#indices}. */
    @NonNull private final List<ParcelableSpan> spans;

    private Spanned(@NonNull IntRange indices, @NonNull List<ParcelableSpan> spans) {
      super(indices);
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

    /**
     * Gets the {@link ParcelableSpan}s that have been applied to the specified {@link
     * Spanned#indices}.
     */
    @NonNull
    List<ParcelableSpan> getSpans() {
      return spans;
    }

    /** Converts this {@link Part} into a {@link Style}. */
    List<Style> toStyles() {
      ArrayList<Style> styles = new ArrayList<>();
      for (ParcelableSpan span : spans) {
        styles.addAll(AnyExtensions.toStyles(span, getIndices()));
      }
      return styles;
    }

    /**
     * Returns whether the given {@link ParcelableSpan}s are structurally equal to the ones that
     * belong to this {@link Spanned}.
     *
     * @param others {@link ParcelableSpan}s to which those of this {@link Spanned} will be
     *     structurally compared.
     */
    private boolean areSpansStructurallyEqual(List<ParcelableSpan> others) {
      if (spans.size() != others.size()) {
        return false;
      }
      for (int index = 0; index < spans.size(); index++) {
        if (!ParcelableSpanExtensions.isStructurallyEqualTo(spans.get(index), others.get(index))) {
          return false;
        }
      }
      return true;
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
   * Creates a {@link Part.Spanned} with the given {@link ParcelableSpan}s.
   *
   * @param spans {@link ParcelableSpan}s that have been applied.
   */
  Spanned span(ParcelableSpan... spans) {
    List<ParcelableSpan> spansAsList = List.of(spans);
    return new Spanned(indices, spansAsList);
  }
}
