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

import android.os.Build;
import android.view.View;
import android.view.WindowInsetsAnimation;
import android.view.WindowInsetsController;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import br.com.orcinus.orca.platform.ime.Ime;
import br.com.orcinus.orca.platform.ime.test.scope.animation.ImeAnimationCallback;
import br.com.orcinus.orca.platform.ime.test.scope.animation.stage.Stage;
import java.time.Duration;
import java.util.function.Consumer;
import kotlin.PublishedApi;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.CoroutineScope;

/** Scope in which {@link Ime} tests can be run. */
public class ImeScope implements CoroutineScope {
  /** {@link ImeScopeActivity} from which the IME will be toggled. */
  @NonNull private final ImeScopeActivity activity;

  /** {@link View} to which the {@link ImeAnimationCallback} will be added. */
  @NonNull private final View view;

  /**
   * {@link CapturingOnImeVisibilityChangeListener} by which the current visibility of the IME is
   * captured and provided.
   *
   * @see Ime.Visibility
   * @see ImeScope#getVisibility()
   */
  @NonNull private final CapturingOnImeVisibilityChangeListener onVisibilityChangeListener;

  /**
   * {@link ImeAnimationCallback} that suspends this {@link ImeScope} while {@link
   * WindowInsetsAnimation}s are ongoing.
   *
   * @see Stage#ongoing(Duration)
   */
  @NonNull private final ImeAnimationCallback animationCallback;

  /** {@link CoroutineScope} to which {@link CoroutineScope}-like behavior is delegated. */
  @NonNull private final CoroutineScope delegate;

  /**
   * Scope in which {@link Ime} tests can be run.
   *
   * @param activity {@link ImeScopeActivity} from which the IME will be toggled.
   * @param view {@link View} to which the {@link ImeAnimationCallback} will be added.
   * @param onVisibilityChangeListener {@link CapturingOnImeVisibilityChangeListener} by which the
   *     current visibility of the IME is captured and provided.
   * @param animationCallback {@link ImeAnimationCallback} that suspends this {@link ImeScope} while
   *     {@link WindowInsetsAnimation}s are ongoing.
   * @param delegate {@link CoroutineScope} to which {@link CoroutineScope}-like behavior is
   *     delegated.
   * @see ImeScope#getVisibility()
   * @see Stage#ongoing(Duration)
   */
  @PublishedApi
  public ImeScope(
      @NonNull ImeScopeActivity activity,
      @NonNull View view,
      @NonNull CapturingOnImeVisibilityChangeListener onVisibilityChangeListener,
      @NonNull ImeAnimationCallback animationCallback,
      @NonNull CoroutineScope delegate) {
    super();
    this.activity = activity;
    this.view = view;
    this.onVisibilityChangeListener = onVisibilityChangeListener;
    this.animationCallback = animationCallback;
    this.delegate = delegate;
  }

  /**
   * Opens the IME.
   *
   * @param continuation {@link Continuation} to resume from after the IME {@link
   *     WindowInsetsAnimation} has ended.
   */
  @RequiresApi(Build.VERSION_CODES.R)
  void open(Continuation<? super Unit> continuation) {
    runAndAwaitAnimation(
        continuation, (windowInsetsController) -> windowInsetsController.show(Ime.type));
  }

  /**
   * Closes the IME.
   *
   * @param continuation {@link Continuation} to resume from after the IME {@link
   *     WindowInsetsAnimation} has ended.
   */
  @RequiresApi(Build.VERSION_CODES.R)
  void close(Continuation<? super Unit> continuation) {
    runAndAwaitAnimation(
        continuation, (windowInsetsController) -> windowInsetsController.hide(Ime.type));
  }

  /**
   * Gets the latest visibility to which the IME has changed.
   *
   * @see Ime.Visibility
   */
  public int getVisibility() {
    return onVisibilityChangeListener.getVisibility();
  }

  @NonNull
  @Override
  public CoroutineContext getCoroutineContext() {
    return delegate.getCoroutineContext();
  }

  /** Gets the {@link View} to which the {@link ImeAnimationCallback} will be added. */
  @NonNull
  public View getView() {
    return view;
  }

  /**
   * Gets the {@link ImeAnimationCallback} that suspends this {@link ImeScope} while {@link
   * WindowInsetsAnimation}s are ongoing.
   *
   * @see Stage#ongoing(Duration)
   */
  @NonNull
  public ImeAnimationCallback getAnimationCallback() {
    return animationCallback;
  }

  /**
   * Performs the given action on the {@link View}'s {@link WindowInsetsController} and awaits the
   * end of the IME {@link WindowInsetsAnimation}.
   *
   * @param action {@link Consumer} that accepts the {@link WindowInsetsController}.
   * @param continuation {@link Continuation} to resume from after the IME {@link
   *     WindowInsetsAnimation} has ended.
   * @see ImeScope#getView()
   * @see View#getWindowInsetsController()
   */
  @RequiresApi(Build.VERSION_CODES.R)
  private void runAndAwaitAnimation(
      @NonNull Continuation<? super Unit> continuation,
      @NonNull Consumer<WindowInsetsController> action) {
    @Nullable WindowInsetsController windowInsetsController = getView().getWindowInsetsController();
    assert windowInsetsController != null;
    activity.runOnUiThread(() -> action.accept(windowInsetsController));
    animationCallback.awaitAnimation(continuation);
  }
}
