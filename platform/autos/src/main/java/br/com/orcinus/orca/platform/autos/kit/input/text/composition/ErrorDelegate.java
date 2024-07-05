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

package br.com.orcinus.orca.platform.autos.kit.input.text.composition;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewKt;
import br.com.orcinus.orca.platform.autos.R;
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.spanned.span.AccessibleObjects;
import java.lang.reflect.Field;
import java.util.function.IntUnaryOperator;
import kotlin.Unit;

/** Helper class for drawing and measuring the error of a {@link CompositionTextField}. */
class ErrorDelegate {
  /** {@link CompositionTextField} whose error is to be managed by this delegate. */
  private final CompositionTextField textField;

  /**
   * {@link TextPaint} for painting the error.
   *
   * @see ErrorDelegate#clearingPaint
   */
  private final TextPaint textPaint;

  /**
   * {@link Paint} for clearing the drawn error in the {@link Canvas}.
   *
   * @see ErrorDelegate#textPaint
   */
  private final Paint clearingPaint;

  /**
   * {@link RectF} whose bottom coordinate is the only one that gets updated on demand and will be
   * equal to the initial value of {@link ErrorDelegate#lastHeight} until the error is shown (since
   * calculating the height requires measuring the actual message). Is {@code null} until the {@link
   * ErrorDelegate#textField} is laid out.
   *
   * @see ErrorDelegate#show(CharSequence)
   * @see ErrorDelegate#initRectWhenLaidOut()
   */
  private RectF rect;

  /** Height of the last error in pixels. */
  private float lastHeight = Float.NaN;

  /**
   * Helper class for drawing and measuring the defined error of a {@link CompositionTextField}.
   *
   * @param textField {@link CompositionTextField} whose error is to be managed by this delegate.
   * @throws NoSuchFieldException If the {@link Field} to which the {@link CompositionTextField}'s
   *     {@link TextPaint} is expected to have been assigned isn't found.
   */
  ErrorDelegate(@NonNull CompositionTextField textField) throws NoSuchFieldException {
    this.textField = textField;
    textPaint = new TextPaint();
    textPaint.set(getTextPaint(textField));
    textPaint.setColor(getColor());
    clearingPaint = new Paint();
    clearingPaint.setColor(CompositionTextField.getBackgroundColor(textField.getContext()));
    initRectWhenLaidOut();
  }

  /**
   * Draws the error on the {@link Canvas}.
   *
   * @param canvas {@link Canvas} on which the error will be drawn.
   */
  void draw(@NonNull Canvas canvas) {
    final CharSequence error = textField.getError();
    final float x = rect.left;
    final float y = rect.top;
    if (error != null) {
      final String text = error.toString();
      canvas.drawText(text, x, y, textPaint);
    } else {
      canvas.drawRect(rect, clearingPaint);
    }
  }

  /**
   * Reacts to a change in the {@link Configuration} by invalidating the drawn error. This method
   * should be called <b>before</b> the actual change occurs.
   *
   * @param configuration {@link Configuration} with the applied modifications.
   */
  void invalidate(@Nullable Configuration configuration) {
    if (didUIModeChange(configuration)) {
      textPaint.setColor(getColor());
    }
  }

  /**
   * Either shows or hides the error based on its nullability.
   *
   * @param error Message stating the invalid state.
   */
  void toggle(@Nullable CharSequence error) {
    if (error != null) {
      show(error);
    } else {
      hide();
    }
  }

  /**
   * Initializes the {@link ErrorDelegate#rect} with the coordinates of the error when the {@link
   * ErrorDelegate#textField} is laid out.
   */
  private void initRectWhenLaidOut() {
    ViewKt.doOnLayout(
        textField,
        _textField -> {
          final Context context = textField.getContext();
          final float spacing = CompositionTextField.getSpacing(context);
          final float top = textField.getHeight() + spacing * 2;
          final float right = textField.getWidth();
          rect = new RectF(spacing, top, right, lastHeight);
          return Unit.INSTANCE;
        });
  }

  /**
   * Obtains the {@link TextPaint} by which the {@link TextView}'s text is painted.
   *
   * @param textView {@link TextView} whose {@link TextPaint} will be returned.
   * @throws NoSuchFieldException If the {@link Field} to which the {@link TextPaint} is expected to
   *     have been assigned isn't found.
   */
  @SuppressWarnings("DiscouragedPrivateApi, JavaReflectionMemberAccess")
  private TextPaint getTextPaint(TextView textView) throws NoSuchFieldException {
    return AccessibleObjects.access(
        TextView.class.getDeclaredField("mTextPaint"),
        (field) -> {
          try {
            return (TextPaint) field.get(textView);
          } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
          }
        });
  }

  /** Obtains the color by which the error is colored. */
  @ColorInt
  private int getColor() {
    // TODO: Turn error color into a single one in αὐτός.
    return textField.getContext().getColor(R.color.errorContainer);
  }

  /**
   * Determines whether the UI mode of the given {@link Configuration} differs from that of the
   * {@link ErrorDelegate#textField}.
   *
   * @param configuration {@link Configuration} whose UI mode will be compared to that of the {@link
   *     ErrorDelegate#textField}'s.
   */
  private boolean didUIModeChange(@Nullable Configuration configuration) {
    return configuration != null
        && (textField.getContext().getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK)
            == (configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK);
  }

  /**
   * Displays the error on the {@link ErrorDelegate#textField}.
   *
   * @param error Message stating the invalid state.
   */
  private void show(@NonNull CharSequence error) {
    final Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
    final float charHeight = fontMetrics.descent - fontMetrics.ascent;
    ViewKt.doOnLayout(
        textField,
        textField -> {
          final int errorLineCount = countLines(error);
          final int errorY = (int) rect.top;
          final int errorHeight = (int) rect.height();
          final Context context = textField.getContext();
          final int spacing = CompositionTextField.getSpacing(context);
          lastHeight = errorLineCount * charHeight + fontMetrics.leading * charHeight;
          rect.bottom = lastHeight;
          animateHeight(textFieldHeight -> errorY + errorHeight + spacing);
          return Unit.INSTANCE;
        });
  }

  /** Hides the error shown by the {@link ErrorDelegate#textField}. */
  private void hide() {
    ViewKt.doOnLayout(
        textField,
        textField -> {
          final int errorHeight = (int) rect.height();
          final Context context = textField.getContext();
          final int spacing = CompositionTextField.getSpacing(context);
          animateHeight(textFieldHeight -> errorHeight - textFieldHeight - spacing);
          return Unit.INSTANCE;
        });
  }

  /**
   * Counts the amount of lines needed to comprise the error.
   *
   * @param error Message stating the invalid state.
   */
  private int countLines(@NonNull CharSequence error) {
    final float lineWidth = rect.width();
    if (lineWidth > 0f) {
      int lineCount = 0;
      final int end = error.length();
      final boolean measuresForwards = true;
      int start = 0;
      do {
        start += textPaint.breakText(error, start, end, measuresForwards, lineWidth, null);
        lineCount++;
      } while (start < end);
      return lineCount;
    }
    return 0;
  }

  /**
   * Animates {@link ErrorDelegate#textField}'s height to the given value.
   *
   * @param height Provides the height to which the current one will be animated.
   */
  private void animateHeight(IntUnaryOperator height) {
    final int currentHeight = textField.getHeight();
    final int animatedHeight = height.applyAsInt(currentHeight);
    final ValueAnimator valueAnimator =
        ValueAnimator.ofInt(currentHeight, animatedHeight).setDuration(256);
    valueAnimator.addUpdateListener(
        updatedValueAnimator -> {
          ViewKt.updateLayoutParams(
              textField,
              layoutParams -> {
                layoutParams.height = (int) updatedValueAnimator.getAnimatedValue();
                return Unit.INSTANCE;
              });
          textField.invalidate();
        });
    valueAnimator.start();
  }
}
