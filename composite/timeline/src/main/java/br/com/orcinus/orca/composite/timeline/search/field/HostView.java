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

package br.com.orcinus.orca.composite.timeline.search.field;

import android.content.Context;
import android.util.DisplayMetrics;
import androidx.annotation.NonNull;
import androidx.compose.foundation.layout.PaddingValues;
import androidx.compose.runtime.CompositionContext;
import androidx.compose.runtime.CompositionLocal;
import androidx.compose.runtime.InternalComposeApi;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.Shape;
import androidx.compose.ui.platform.AbstractComposeView;
import br.com.orcinus.orca.platform.autos.kit.input.text.SearchTextFieldKt;
import com.jeanbarrossilva.loadable.list.ListLoadable;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

/**
 * {@link AbstractComposeView} by which a search text field is to be hosted and shown in a popup,
 * whose width is unbounded to its delegate's default one and layout can be observed. These
 * workarounds leverage the public visibility in Java of both its on-measure and on-layout callbacks
 * declared internal from Kotlin.
 *
 * @see SearchTextFieldKt#SearchTextField(String, Function1, boolean, Modifier, Shape, float,
 *     PaddingValues, Function0)
 * @see SearchTextFieldPopupKt#SearchTextFieldPopup(String, Function1, ListLoadable, Function0,
 *     Modifier, Alignment, long, PaddingValues)
 */
abstract class HostView extends AbstractComposeView {
  /**
   * Constructs a {@link HostView} in the given {@link Context}, setting its parent {@link
   * CompositionContext} for the composable content to "inherit" the {@link CompositionLocal}s
   * provided by it.
   *
   * @param context {@link Context} to run in and from which theme and resources can be accessed.
   * @param parentCompositionContext {@link CompositionContext} that is the parent of this {@link
   *     HostView}'s.
   */
  public HostView(
      @NonNull final Context context, @NonNull final CompositionContext parentCompositionContext) {
    super(context);
    setParentCompositionContext(parentCompositionContext);
  }

  @InternalComposeApi
  @Override
  public final void internalOnMeasure$ui_release(
      final int widthMeasureSpec, final int heightMeasureSpec) {
    final DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
    final int unboundedWidthMeasureSpec =
        MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, MeasureSpec.AT_MOST);
    super.internalOnMeasure$ui_release(unboundedWidthMeasureSpec, heightMeasureSpec);
  }

  @InternalComposeApi
  @Override
  public final void internalOnLayout$ui_release(
      final boolean changed, final int left, final int top, final int right, final int bottom) {
    super.internalOnLayout$ui_release(changed, left, top, right, bottom);
    onLaidOut(changed);
  }

  /**
   * Callback called <i>after</i> this {@link HostView} has been laid out.
   *
   * @param isChanged Whether repositioned or resized.
   */
  protected abstract void onLaidOut(final boolean isChanged);
}
