/*
 * Copyright © 2023 Orca
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

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.bottom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import br.com.orcinus.orca.autos.borders.Border;
import br.com.orcinus.orca.autos.borders.Borders;
import br.com.orcinus.orca.autos.colors.Colors;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jeanbarrossilva.orca.platform.autos.R;
import com.jeanbarrossilva.orca.platform.autos.borders.BordersExtensions;
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme;

/** Orca-specific {@link BottomNavigationView}. */
public final class OrcaBottomNavigationView extends BottomNavigationView {
  /** {@link Paint} by which the divider will be painted. */
  private final Paint dividerPaint = new Paint();

  /** Height of the divider in pixels. */
  private final int dividerHeight;

  public OrcaBottomNavigationView(@NonNull Context context) {
    this(context, null);
  }

  public OrcaBottomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public OrcaBottomNavigationView(
      @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    Colors colors = AutosTheme.INSTANCE.getColors(context);
    Border defaultBorder = Borders.Companion.getDefault(colors).getDefault();
    int defaultMediumBorderColor =
        defaultBorder.getColor() != null ? defaultBorder.getColor().intValue() : Color.TRANSPARENT;
    int defaultMediumBorderWidth = (int) defaultBorder.getWidth();
    dividerPaint.setColor(defaultMediumBorderColor);
    dividerHeight =
        BordersExtensions.areApplicable(Borders.Companion, context)
            ? Units.dp(context, defaultMediumBorderWidth)
            : 0;
    setBackgroundColor(getContext().getColor(R.color.surfaceContainer));
    stylizeItems();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    heightMeasureSpec =
        MeasureSpec.makeMeasureSpec(
            MeasureSpec.getSize(heightMeasureSpec) + dividerHeight,
            MeasureSpec.getMode(heightMeasureSpec));
    setMeasuredDimension(
        MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override
  protected void onDraw(@NonNull Canvas canvas) {
    drawDivider(canvas);
    super.onDraw(canvas);
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
              getContext().getColor(R.color.backgroundContent),
              getContext().getColor(R.color.secondary)
            }));
    setLabelVisibilityMode(LABEL_VISIBILITY_UNLABELED);
  }

  /** Draws the divider. */
  private void drawDivider(@NonNull Canvas canvas) {
    canvas.drawRect(0f, 0f, getWidth(), dividerHeight, dividerPaint);
    canvas.translate(0f, dividerHeight);
  }
}
