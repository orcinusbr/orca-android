/*
 * Copyright © 2024 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.platform.ime.scope;

import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import com.jeanbarrossilva.orca.platform.ime.Ime;
import com.jeanbarrossilva.orca.platform.ime.state.OnImeVisibilityChangeListener;

/**
 * {@link OnImeVisibilityChangeListener} that captures the last visibility to which the IME has been
 * set.
 *
 * <p>Since it acts both as a {@link WindowInsetsController.OnControllableInsetsChangedListener} and
 * a {@link WindowInsetsControllerCompat.OnControllableInsetsChangedListener} (the latter being
 * because {@link OnImeVisibilityChangeListener} implements it), the {@link WindowInsetsController}
 * by which controllable {@link Insets} are changed will be converted into its compatibility
 * version.
 *
 * @see Ime.Visibility
 * @see CapturingOnImeVisibilityChangeListener#getVisibility()
 * @see
 *     WindowInsetsController.OnControllableInsetsChangedListener#onControllableInsetsChanged(WindowInsetsController,
 *     int)
 */
public class CapturingOnImeVisibilityChangeListener extends OnImeVisibilityChangeListener
    implements WindowInsetsController.OnControllableInsetsChangedListener {
  /**
   * {@link Activity} whose {@link Window} will be used to convert a {@link WindowInsetsController}
   * into a {@link WindowInsetsControllerCompat}.
   *
   * @see Activity#getWindow()
   * @see WindowCompat#getInsetsController(Window, View)
   */
  @NonNull private final Activity activity;

  /**
   * Latest visibility to which the IME has changed.
   *
   * @see Ime.Visibility
   */
  private int visibility = Ime.Visibility.UNKNOWN;

  /**
   * {@link OnImeVisibilityChangeListener} that captures the last visibility to which the IME has
   * been set.
   *
   * <p>Since it acts both as a {@link WindowInsetsController.OnControllableInsetsChangedListener}
   * and a {@link WindowInsetsControllerCompat.OnControllableInsetsChangedListener} (the latter
   * being because {@link OnImeVisibilityChangeListener} implements it), the {@link
   * WindowInsetsController} by which controllable {@link Insets} are changed will be converted into
   * its compatibility version.
   *
   * @param activity {@link Activity} whose {@link Window} will be used to convert a {@link
   *     WindowInsetsController} into a {@link WindowInsetsControllerCompat}.
   * @param view {@link View} whose {@link WindowInsetsCompat} will provide the visibility.
   * @see Ime.Visibility
   * @see CapturingOnImeVisibilityChangeListener#getVisibility()
   * @see
   *     WindowInsetsController.OnControllableInsetsChangedListener#onControllableInsetsChanged(WindowInsetsController,
   *     int)
   */
  public CapturingOnImeVisibilityChangeListener(@NonNull Activity activity, @NonNull View view) {
    super(view);
    this.activity = activity;
  }

  @Override
  public void onImeVisibilityChange(int ime) {
    visibility = ime;
  }

  @Override
  public void onControllableInsetsChanged(
      @NonNull WindowInsetsController controller, int typeMask) {
    Window window = activity.getWindow();
    View view = getView();
    WindowInsetsControllerCompat controllerCompat = WindowCompat.getInsetsController(window, view);
    onControllableInsetsChanged(controllerCompat, typeMask);
  }

  /**
   * Gets the latest visibility to which the IME has changed.
   *
   * @see Ime.Visibility
   */
  @Ime.Visibility
  int getVisibility() {
    return visibility;
  }
}
