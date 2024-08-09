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

package br.com.orcinus.orca.core.sample.instance.registration

import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.instance.registration.Credentials
import br.com.orcinus.orca.core.instance.registration.Registrar
import br.com.orcinus.orca.core.instance.registration.Registration
import br.com.orcinus.orca.core.sample.instance.domain.sample
import br.com.orcinus.orca.core.sample.instance.domain.samples

/**
 * [Registrar] that emits a successful [Registration] for a sample [Domain].
 *
 * @see Instance.domain
 */
internal object SampleRegistrar : Registrar() {
  override val domains = Domain.samples

  override suspend fun register(credentials: Credentials, domain: Domain): Boolean {
    return domain == Domain.sample
  }
}
