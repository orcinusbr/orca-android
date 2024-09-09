/*
 * Copyright © 2015 Christophe Smet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *                           http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ================================================================================================
 *
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

package br.com.orcinus.orca.app.activity.masking;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/*
 * Orca-specific changes:
 *
 * -  Annotated non-null parameters of overridden methods with androidx.annotation.NonNull;
 * -  Fixed grammatical inconsistencies in logs;
 * -  Referenced original source at GitHub;
 * -  Reformatted class creation date in its Javadoc as per ISO 8601;
 * -  Removed API level checks for <= 28 (P);
 * -  Removed redundant comments;
 * -  Removed void MaskableFrameLayout#setMask(Int);
 * -  Removed software-layering;
 * -  Removed support for anti-aliasing;
 * -  Removed support for add, clear, darken, dest, dest-atop, dest-out, dest-over, lighten,
 *    multiply, overlay, screen, src, src-atop, src-in, src-out, src-over and xor Porter/Duff modes;
 * -  Removed support for XML-defined mask;
 * -  Matched void MaskableFrameLayout#invalidateDrawable(Drawable)'s parameter name with overridden
 *    method's;
 * -  Replaced call to Drawable Resources#getDrawable(int) by one to Drawable
 *    ContextCompat#getDrawable(Resources, int);
 * -  Replaced usage of support libraries (android.support.*) by that of AndroidX ones (androidx.*);
 * -  Turned void MaskableFrameLayout#invalidateDrawable(Drawable) into a proxy for void
 *    MaskableFrameLayout#setMask(Drawable).
 */

/**
 * Created by Christophe on 2014-07-12.
 *
 * @see <a
 *     href="https://github.com/christophesmet/android_maskable_layout/blob/1347a32cda05d479b2a93f77cc70b26aa84f758b/library/src/main/java/com/christophesmet/android/views/maskableframelayout/MaskableFrameLayout.java">Source</a>
 */
public class MaskableFrameLayout extends FrameLayout {
  private static final String TAG = "MaskableFrameLayout";
  private Handler mHandler;
  @Nullable private Drawable mDrawableMask = null;
  @Nullable private Bitmap mFinalMask = null;
  private Paint mPaint = null;
  private final PorterDuffXfermode mPorterDuffXferMode =
      new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

  public MaskableFrameLayout(Context context) {
    super(context);
  }

  public MaskableFrameLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    construct();
  }

  public MaskableFrameLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    construct();
  }

  private void construct() {
    mHandler = new Handler();
    mPaint = createPaint();

    // Once inflated we have no height or width for the mask. Wait for the layout.
    registerMeasure();
  }

  @NonNull
  private Paint createPaint() {
    Paint output = new Paint();
    output.setXfermode(mPorterDuffXferMode);
    return output;
  }

  private void initMask(@Nullable Drawable input) {
    if (input != null) {
      mDrawableMask = input;
      if (mDrawableMask instanceof AnimationDrawable) {
        mDrawableMask.setCallback(this);
      }
    } else {
      log("Are you sure you don't want to provide a mask?");
    }
  }

  @Nullable
  public Drawable getDrawableMask() {
    return mDrawableMask;
  }

  @Nullable
  private Bitmap makeBitmapMask(@Nullable Drawable drawable) {
    if (drawable != null) {
      if (getMeasuredWidth() > 0 && getMeasuredHeight() > 0) {
        Bitmap mask =
            Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mask);
        drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
        drawable.draw(canvas);
        return mask;
      } else {
        log(
            "Can't create a mask with zeroed width or height or if the layout has no children and "
                + "wraps its content.");
        return null;
      }
    } else {
      log("No bitmap mask loaded, view will not be masked!");
    }
    return null;
  }

  public void setMask(@Nullable Drawable input) {
    initMask(input);
    swapBitmapMask(makeBitmapMask(input));
    invalidate();
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    setSize(w, h);
  }

  private void setSize(int width, int height) {
    if (width > 0 && height > 0) {
      if (mDrawableMask != null) {
        swapBitmapMask(makeBitmapMask(mDrawableMask));
      }
    } else {
      log("Width and height must each be greater than 0.");
    }
  }

  @Override
  protected void dispatchDraw(@NonNull Canvas canvas) {
    super.dispatchDraw(canvas);
    if (mFinalMask != null && mPaint != null) {
      mPaint.setXfermode(mPorterDuffXferMode);
      canvas.drawBitmap(mFinalMask, 0.0f, 0.0f, mPaint);
      mPaint.setXfermode(null);
    } else {
      log("Mask or paint is null…");
    }
  }

  private void registerMeasure() {
    final ViewTreeObserver treeObserver = MaskableFrameLayout.this.getViewTreeObserver();
    if (treeObserver != null && treeObserver.isAlive()) {
      treeObserver.addOnGlobalLayoutListener(
          new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
              ViewTreeObserver aliveObserver = treeObserver;
              if (!aliveObserver.isAlive()) {
                aliveObserver = MaskableFrameLayout.this.getViewTreeObserver();
              }
              if (aliveObserver != null) {
                aliveObserver.removeOnGlobalLayoutListener(this);
              } else {
                log("GlobalLayoutListener not removed as ViewTreeObserver is not valid.");
              }
              swapBitmapMask(makeBitmapMask(mDrawableMask));
            }
          });
    }
  }

  private void log(@NonNull String message) {
    Log.d(TAG, message);
  }

  @Override
  public void invalidateDrawable(@Nullable Drawable drawable) {
    setMask(drawable);
  }

  @Override
  public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
    mHandler.postAtTime(what, when);
  }

  @Override
  public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
    mHandler.removeCallbacks(what);
  }

  private void swapBitmapMask(@Nullable Bitmap newMask) {
    if (newMask != null) {
      if (mFinalMask != null && !mFinalMask.isRecycled()) {
        mFinalMask.recycle();
      }
      mFinalMask = newMask;
    }
  }
}
