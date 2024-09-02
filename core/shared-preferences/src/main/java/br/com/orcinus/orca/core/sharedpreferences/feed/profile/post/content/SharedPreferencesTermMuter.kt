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

package br.com.orcinus.orca.core.sharedpreferences.feed.profile.post.content

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting
import androidx.core.content.edit
import br.com.orcinus.orca.core.feed.profile.post.content.TermMuter

/**
 * [TermMuter] that persists muted terms through the [SharedPreferences] API.
 *
 * @param context [Context] through which the [SharedPreferences] will be created.
 */
class SharedPreferencesTermMuter(private val context: Context) : TermMuter() {
  override val initialTerms
    @Suppress("UNCHECKED_CAST")
    get() = preferences.all.map(Map.Entry<String, Any?>::value).toHashSet() as HashSet<String>

  /** [SharedPreferences] into which the muted terms are persisted. */
  @VisibleForTesting
  internal val preferences
    get() = context.getSharedPreferences("term-muter", Context.MODE_PRIVATE)

  override suspend fun onMuting(term: String) {
    preferences.edit { putString(term, term) }
  }

  override suspend fun onUnmuting(term: String) {
    preferences.edit { remove(term) }
  }

  /** Clears all persisted terms. */
  fun reset() {
    preferences.edit(action = SharedPreferences.Editor::clear)
  }
}
