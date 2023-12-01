/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
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
