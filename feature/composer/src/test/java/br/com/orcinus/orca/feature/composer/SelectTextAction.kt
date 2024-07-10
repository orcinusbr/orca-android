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

package br.com.orcinus.orca.feature.composer

import android.view.View
import android.widget.EditText
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import org.hamcrest.Matcher

/**
 * [ViewAction] that selects a portion of an [EditText]'s text.
 *
 * @property selection Indices to be selected.
 */
private class SelectTextAction(private val selection: IntRange) : ViewAction {
  override fun getDescription(): String {
    return "select text($selection)"
  }

  override fun getConstraints(): Matcher<View> {
    return isAssignableFrom(EditText::class.java)
  }

  override fun perform(uiController: UiController?, view: View?) {
    (view as EditText?)?.setSelection(selection.first, selection.last)
  }
}

/**
 * Creates a [ViewAction] that selects a portion of an [EditText]'s text.
 *
 * @param selection Index to be selected.
 */
fun selectText(selection: Int): ViewAction {
  return SelectTextAction(selection..selection)
}

/**
 * Creates a [ViewAction] that selects a portion of an [EditText]'s text.
 *
 * @param selection Indices to be selected.
 */
fun selectText(selection: IntRange): ViewAction {
  return SelectTextAction(selection)
}
