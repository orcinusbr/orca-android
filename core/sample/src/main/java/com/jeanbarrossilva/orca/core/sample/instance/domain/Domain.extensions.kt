package com.jeanbarrossilva.orca.core.sample.instance.domain

import com.jeanbarrossilva.orca.core.instance.domain.Domain

/** [Domain]s returned by [sample]s. */
@Suppress("SpellCheckingInspection")
private val sampleDomains =
  listOf(
    Domain("instance.sample"),
    Domain("kpop.social"),
    Domain("linuxrocks.online"),
    Domain("mas.to"),
    Domain("mastodon.cloud"),
    Domain("mastodon.online"),
    Domain("mastodon.social"),
    Domain("mastodon.world"),
    Domain("mstdn.plus"),
    Domain("mstdn.social"),
    Domain("sciences.social"),
    Domain("techhub.social"),
    Domain("universeodon.com")
  )

/** Sample [Domain]. */
val Domain.Companion.sample
  get() = sampleDomains.first()

/** Sample [Domain]s. */
val Domain.Companion.samples
  get() = sampleDomains
