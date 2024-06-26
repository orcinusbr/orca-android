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
import android.text.Spanned;
import androidx.annotation.NonNull;
import br.com.orcinus.orca.platform.markdown.spanned.span.AnyExtensions;
import br.com.orcinus.orca.std.markdown.style.Style;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kotlin.ranges.IntRange;

/** Portion of a {@link android.text.Spanned} that can have spans applied to it. */
public class IndexedSpans {
  /**
   * {@link Context} with which two spans can be compared structurally.
   *
   * @see AnyExtensions#isStructurallyEqual(Object, Context, Object)
   */
  @NonNull private final Context context;

  /** Indices in which the spans are. */
  @NonNull private final IntRange indices;

  /** Spans that have been applied to the specified {@link IndexedSpans#indices}. */
  @NonNull private final List<Object> spans;

  /**
   * Portion of a {@link android.text.Spanned} that can have spans applied to it.
   *
   * @param context {@link Context} with which two spans can be compared structurally.
   * @param indices Indices in which the spans are.
   * @param spans Spans that have been applied to the specified {@link IndexedSpans#indices}.
   * @see AnyExtensions#isStructurallyEqual(Object, Context, Object)
   * @see IndexedSpans#getSpans()
   */
  IndexedSpans(@NonNull Context context, @NonNull IntRange indices, @NonNull List<Object> spans) {
    this.context = context;
    this.indices = indices;
    this.spans = spans;
  }

  /**
   * Portion of a {@link android.text.Spanned} that can have spans applied to it.
   *
   * @param context {@link Context} with which two spans can be compared structurally.
   * @param indices Indices in which the spans are.
   * @param spans Spans that has been applied to the specified {@link IndexedSpans#indices}.
   * @see AnyExtensions#isStructurallyEqual(Object, Context, Object)
   * @see IndexedSpans#getSpans()
   */
  IndexedSpans(@NonNull Context context, @NonNull IntRange indices, @NonNull Object... spans) {
    this(context, indices, List.of(spans));
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof IndexedSpans
        && context == ((IndexedSpans) other).context
        && getIndices().equals(((IndexedSpans) other).getIndices())
        && areSpansStructurallyEqual(((IndexedSpans) other).spans);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getIndices(), spans);
  }

  @NonNull
  @Override
  public String toString() {
    return String.format(
        "IndexedSpans(context=%s, indices=%s, spans=%s)", context, getIndices(), getSpans());
  }

  /** Gets the spans that have been applied to the specified {@link IndexedSpans#indices}. */
  @NonNull
  public List<Object> getSpans() {
    return spans;
  }

  /** Gets the indices to which this {@link IndexedSpans} refers. */
  @NonNull
  public IntRange getIndices() {
    return indices;
  }

  /** Converts the spans into {@link Style}s. */
  @NonNull
  List<Style> toStyles() {
    List<Object> spans = getSpans();
    ArrayList<Style> styles = new ArrayList<>();
    for (Object span : spans) {
      List<Style> spanStyles =
          br.com.orcinus.orca.platform.markdown.annotated.span.AnyExtensions.toStyles(
              span, indices);
      styles.addAll(spanStyles);
    }
    return styles;
  }

  /**
   * Returns whether the given spans are structurally equal to the ones that belong to this {@link
   * Spanned}.
   *
   * @param others Spans to which those of this {@link IndexedSpans} will be structurally compared.
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
