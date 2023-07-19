package com.jeanbarrossilva.mastodonte.platform.ui

import com.jeanbarrossilva.mastodonte.core.toot.Account

/** Deals with [Account]-related formatting. **/
object AccountFormatter {
    /** Formats the [account]'s [username][Account.username] so that it is displayable. **/
    fun username(account: Account): String {
        return "@${account.username}"
    }
}
