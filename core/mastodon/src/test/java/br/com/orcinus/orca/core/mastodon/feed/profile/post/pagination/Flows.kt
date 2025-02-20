/*
 * Copyright © 2025 Orcinus
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

package br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination

import app.cash.turbine.TurbineTestContext

/**
 * When awaiting an item that does not get emitted, Turbine throws an [Exception] of its own,
 * shadowing the actual one which was originally thrown — the cause. This method circumvents such
 * behavior, throwing such cause in case an unexpected event (absence of emission, error or
 * completion) occurs.
 *
 * @param T Item to be awaited.
 * @see TurbineTestContext.awaitItem
 */
@Throws
internal suspend fun <T> TurbineTestContext<T>.awaitItemOrThrowCause() =
  try {
    awaitItem()
  } catch (error: AssertionError) {
    throw checkNotNull(error.cause)
  }
