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

package br.com.orcinus.orca.composite.timeline.search.field

import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Immutable
import androidx.core.view.children
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import java.util.Objects

/** Owner of the tree of a [View]. */
@Immutable
internal abstract class ViewTreeOwner private constructor() :
  ViewModelStoreOwner, SavedStateRegistryOwner {
  override fun equals(other: Any?) =
    other is ViewTreeOwner &&
      lifecycle == other.lifecycle &&
      viewModelStore == other.viewModelStore &&
      savedStateRegistry == other.savedStateRegistry

  override fun hashCode() = Objects.hash(lifecycle, viewModelStore, savedStateRegistry)

  /**
   * Sets the receiver as the owner of the [view]'s tree.
   *
   * @param view [View] whose tree is to be owned by this [ViewTreeOwner].
   */
  internal fun own(view: View) {
    view.setViewTreeLifecycleOwner(this)
    view.setViewTreeViewModelStoreOwner(this)
    view.setViewTreeSavedStateRegistryOwner(this)
  }

  companion object {
    /**
     * Creates a [ViewTreeOwner] from that of a [View].
     *
     * @param view [View] whose tree owners will be combined into a single [ViewTreeOwner].
     */
    @JvmStatic
    fun of(view: View): ViewTreeOwner? {
      val lifecycleOwner = view.findViewTreeLifecycleOwner()
      val viewModelStoreOwner = view.findViewTreeViewModelStoreOwner()
      val savedStateRegistryOwner = view.findViewTreeSavedStateRegistryOwner()
      return if (
        lifecycleOwner != null && viewModelStoreOwner != null && savedStateRegistryOwner != null
      ) {
        object :
          ViewTreeOwner(),
          LifecycleOwner by lifecycleOwner,
          ViewModelStoreOwner by viewModelStoreOwner,
          SavedStateRegistryOwner by savedStateRegistryOwner {
          override val lifecycle: Lifecycle
            get() = lifecycleOwner.lifecycle
        }
      } else {
        null
      }
    }
  }
}

/**
 * Performs the given action on all this [View]'s descendants, be them direct (children of its own)
 * or indirect (children of its children). Does so exclusively, meaning that the [View] on which
 * this method is first called _will not_ be passed into the lambda.
 *
 * @param onTraversal Callback called on each descendant (this [View]'s children, their children,
 *   their children's children, …, recursively).
 */
internal fun View.traverse(onTraversal: (descendant: View) -> Unit) {
  if (this is ViewGroup) {
    for (child in children) {
      onTraversal(child)
      child.traverse(onTraversal)
    }
  }
}
