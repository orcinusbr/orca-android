package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style.type

internal class TargetNotFoundException(match: String) :
    NoSuchElementException("Target not found within \"$match\".")
