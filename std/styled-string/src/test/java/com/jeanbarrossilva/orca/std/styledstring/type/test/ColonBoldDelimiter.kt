package com.jeanbarrossilva.orca.std.styledstring.type.test

import com.jeanbarrossilva.orca.std.styledstring.type.Bold

internal object ColonBoldDelimiter : Bold.Delimiter.Variant() {
  private val delegate = surroundedBy(':')

  override fun getRegex(): Regex {
    return delegate.regex
  }

  override fun onGetTarget(match: String): String {
    return delegate.getTarget(match)
  }

  override fun onTarget(target: String): String {
    return delegate.target(target)
  }
}
