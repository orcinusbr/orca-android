package com.jeanbarrossilva.orca.platform.ui

import com.jeanbarrossilva.orca.core.feed.profile.account.Account

/** Deals with [Account]-related formatting. */
object AccountFormatter {
  /** Formats the [account]'s [username][Account.username] so that it is displayable. */
  fun username(account: Account): String {
    return "@${account.username}"
  }
}
