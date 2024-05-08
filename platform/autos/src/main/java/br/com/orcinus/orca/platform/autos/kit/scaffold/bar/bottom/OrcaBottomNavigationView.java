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

package br.com.orcinus.orca.platform.autos.kit.scaffold.bar.bottom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import br.com.orcinus.orca.autos.colors.Colors;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/** Orca-specific {@link BottomNavigationView}. */
public final class OrcaBottomNavigationView extends BottomNavigationView {
  public OrcaBottomNavigationView(@NonNull Context context) {
    this(context, null);
  }

  public OrcaBottomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public OrcaBottomNavigationView(
      @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setBackgroundColor(Colors.LIGHT.getPrimary().getContainer().intValue());
    stylizeItems();
  }

  /** Stylizes the items. */
  private void stylizeItems() {
    setItemActiveIndicatorEnabled(false);
    setItemIconTintList(
        new ColorStateList(
            new int[][] {
              new int[] {android.R.attr.state_selected}, new int[] {-android.R.attr.state_selected}
            },
            new int[] {
              Colors.LIGHT.getPrimary().getContent().intValue(), (int) Colors.LIGHT.getSecondary()
            }));
    setLabelVisibilityMode(LABEL_VISIBILITY_UNLABELED);
  }
}
