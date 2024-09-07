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

package br.com.orcinus.orca.app.activity.masking.scope;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import br.com.orcinus.orca.app.activity.masking.MaskableFrameLayout;
import br.com.orcinus.orca.app.activity.masking.Masker;
import br.com.orcinus.orca.ext.reflection.java.AccessibleObjects;
import kotlin.jvm.functions.Function1;

/**
 * Scope in which a test that targets {@link Masker} functionality is run.
 *
 * @see MaskerScopeKt#runMaskerTest(Function1)
 */
public class MaskerScope {
  /** Radii of an unset mask, an array containing 8 NaNs. */
  static final float[] UNSET_MASK_RADII =
      new float[] {
        Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN
      };

  /** {@link MaskableFrameLayout} on which the mask is to be applied. */
  public final MaskableFrameLayout view;

  /** {@link Activity} that is started by a {@link Masker} test. */
  public static class MaskingActivity extends Activity {}

  /**
   * Scope in which a test that targets {@link Masker} functionality is run.
   *
   * @param view {@link MaskableFrameLayout} on which the mask is to be applied.
   * @see MaskerScopeKt#runMaskerTest(Function1)
   */
  MaskerScope(MaskableFrameLayout view) {
    this.view = view;
  }

  /**
   * Obtains the radii of each corner (top left, top right, bottom right and bottom left) of the
   * mask that is currently applied to the {@link MaskerScope#view}. Returns {@link
   * MaskerScope#UNSET_MASK_RADII} if they cannot be retrieved.
   */
  public float[] getMaskRadii() {
    final Drawable mask = view.getDrawableMask();
    if (!(mask instanceof ShapeDrawable)) {
      return UNSET_MASK_RADII;
    }
    final Shape shape = ((ShapeDrawable) mask).getShape();
    if (!(shape instanceof RoundRectShape)) {
      return UNSET_MASK_RADII;
    }
    try {
      //noinspection JavaReflectionMemberAccess
      return AccessibleObjects.access(
          RoundRectShape.class.getDeclaredField("mOuterRadii"),
          (outerRadiiField) -> {
            try {
              return (float[]) outerRadiiField.get(shape);
            } catch (IllegalAccessException exception) {
              return UNSET_MASK_RADII;
            }
          });
    } catch (NoSuchFieldException exception) {
      return UNSET_MASK_RADII;
    }
  }
}
