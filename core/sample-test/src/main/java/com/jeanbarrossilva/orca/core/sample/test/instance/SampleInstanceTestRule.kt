/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.core.sample.test.instance

import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.instance.SampleInstance
import org.junit.rules.ExternalResource

/**
 * [ExternalResource] that resets the [Instance.Companion.sample]'s writers (such as
 * [SampleInstance.profileWriter] and [SampleInstance.postWriter]) at the end of every test.
 */
class SampleInstanceTestRule(private val instance: SampleInstance = Instance.sample) :
  ExternalResource() {
  override fun after() {
    Instance.sample.profileWriter.reset()
    Instance.sample.postWriter.reset()
  }
}
