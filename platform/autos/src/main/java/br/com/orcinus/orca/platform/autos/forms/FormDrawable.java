/*
 * Copyright © 2025 Orcinus
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

package br.com.orcinus.orca.platform.autos.forms;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import br.com.orcinus.orca.autos.forms.Form;
import br.com.orcinus.orca.platform.autos.Units;
import java.util.Arrays;

/**
 * {@link Drawable} that clips another one, rounding its corners according to a {@link Form}.
 *
 * @see #delegate
 */
final class FormDrawable extends Drawable {
  /** {@link Drawable} whose corners are rounded by this one. */
  private final Drawable delegate;

  /**
   * {@link Path} to which the uneven round {@link Rect} is added when this is drawn.
   *
   * @see #isUniform
   */
  private final Path path = new Path();

  /**
   * Four pairs of two radii — an X- and a Y-axis one — in pixels based on which the corners are
   * rounded.
   */
  private final float[] radii = new float[8];

  /**
   * Whether the corners' radii are all equal to each other.
   *
   * @see #radii
   */
  private final boolean isUniform;

  /**
   * {@link Drawable} that clips another one, rounding its corners according to a {@link Form}.
   *
   * @param context {@link Context} for converting density-independent pixels into absolute ones.
   * @param form {@link Form} by which the {@link #delegate} will be clipped.
   * @param delegate {@link Drawable} whose corners are rounded by this one.
   */
  FormDrawable(
      @NonNull final Context context,
      @NonNull final Form.PerCorner form,
      @NonNull final Drawable delegate) {
    this.delegate = delegate;
    isUniform =
        form.getTopStart() == form.getTopEnd()
            && form.getTopEnd() == form.getBottomEnd()
            && form.getBottomEnd() == form.getBottomStart();
    final float topStartRadiusInPx = Units.dp(context, form.getTopStart());
    if (isUniform) {
      Arrays.fill(radii, topStartRadiusInPx);
    } else {
      radii[0] = topStartRadiusInPx;
      radii[1] = (float) Units.dp(context, form.getTopStart());
      radii[2] = (float) Units.dp(context, form.getTopEnd());
      radii[3] = (float) Units.dp(context, form.getTopEnd());
      radii[4] = (float) Units.dp(context, form.getBottomEnd());
      radii[5] = (float) Units.dp(context, form.getBottomEnd());
      radii[6] = (float) Units.dp(context, form.getBottomStart());
      radii[7] = (float) Units.dp(context, form.getBottomStart());
    }
    delegate.copyBounds(getBounds());
  }

  @Override
  public void draw(@NonNull final Canvas canvas) {
    final Rect bounds = getBounds();
    path.reset();
    path.addRoundRect(
        (float) bounds.left,
        (float) bounds.top,
        (float) bounds.right,
        (float) bounds.bottom,
        radii,
        Path.Direction.CW);
    canvas.save();
    canvas.clipPath(path);
    delegate.setBounds(bounds);
    delegate.draw(canvas);
    canvas.restore();
  }

  @Override
  public void setBounds(final int left, final int top, final int right, final int bottom) {
    super.setBounds(left, top, right, bottom);
    delegate.setBounds(left, top, right, bottom);
  }

  @Override
  public void getOutline(@NonNull final Outline outline) {
    if (isUniform) {
      outline.setRoundRect(getBounds(), radii[0]);
    } else {
      outline.setRect(getBounds());
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        outline.setPath(path);
      }
    }
    outline.setAlpha(0f);
  }

  @Override
  public void setAlpha(final int alpha) {
    delegate.setAlpha(alpha);
  }

  @Override
  public void setColorFilter(@Nullable final ColorFilter colorFilter) {
    delegate.setColorFilter(colorFilter);
  }

  @Override
  public int getOpacity() {
    return delegate.getOpacity();
  }
}
