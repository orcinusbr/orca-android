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

package br.com.orcinus.orca.platform.ime.test.scope;

import android.view.View;
import android.view.WindowInsetsController;
import androidx.annotation.NonNull;
import androidx.core.view.WindowInsetsCompat;
import br.com.orcinus.orca.platform.ime.Ime;
import br.com.orcinus.orca.platform.ime.state.OnImeVisibilityChangeListener;

/**
 * {@link OnImeVisibilityChangeListener} that captures the last visibility to which the IME has been
 * set.
 *
 * @see Ime.Visibility
 * @see CapturingOnImeVisibilityChangeListener#getVisibility()
 * @see
 *     WindowInsetsController.OnControllableInsetsChangedListener#onControllableInsetsChanged(WindowInsetsController,
 *     int)
 */
public class CapturingOnImeVisibilityChangeListener extends OnImeVisibilityChangeListener {
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
   * @param view {@link View} whose {@link WindowInsetsCompat} will provide the visibility.
   * @see Ime.Visibility
   * @see CapturingOnImeVisibilityChangeListener#getVisibility()
   * @see
   *     WindowInsetsController.OnControllableInsetsChangedListener#onControllableInsetsChanged(WindowInsetsController,
   *     int)
   */
  public CapturingOnImeVisibilityChangeListener(@NonNull View view) {
    super(view);
  }

  @Override
  public void onImeVisibilityChange(int ime) {
    visibility = ime;
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
