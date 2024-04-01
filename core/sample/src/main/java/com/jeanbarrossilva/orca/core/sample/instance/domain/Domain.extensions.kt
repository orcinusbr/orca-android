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
