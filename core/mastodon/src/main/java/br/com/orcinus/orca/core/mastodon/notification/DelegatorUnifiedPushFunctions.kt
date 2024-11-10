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

package br.com.orcinus.orca.core.mastodon.notification

import android.content.Context
import org.unifiedpush.android.connector.UnifiedPush
import org.unifiedpush.android.connector.ui.UnifiedPushFunctions

/**
 * Functions which delegate to their equivalents on [UnifiedPush].
 *
 * @property context [Context] to be passed in to the delegated functions.
 */
internal class DelegatorUnifiedPushFunctions(private val context: Context) : UnifiedPushFunctions {
  override fun getAckDistributor() = UnifiedPush.getAckDistributor(context)

  override fun getDistributors() = UnifiedPush.getDistributors(context)

  override fun registerApp(instance: String) = UnifiedPush.registerApp(context, instance)

  override fun saveDistributor(distributor: String) =
    UnifiedPush.saveDistributor(context, distributor)

  override fun tryUseDefaultDistributor(callback: (Boolean) -> Unit) =
    UnifiedPush.tryUseDefaultDistributor(context, callback)
}
