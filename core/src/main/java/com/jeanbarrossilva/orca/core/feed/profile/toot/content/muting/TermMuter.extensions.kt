package com.jeanbarrossilva.orca.core.feed.profile.toot.content.muting

/**
 * Mutes and retrieves terms that have been muted.
 *
 * @param build Configuration for the [TermMuter].
 */
fun TermMuter(build: TermMuter.Builder.() -> Unit): TermMuter {
  return TermMuter.Builder().apply(build).build()
}
