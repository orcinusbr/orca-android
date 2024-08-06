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

package br.com.orcinus.orca.core.sample.test.feed.profile.type

import br.com.orcinus.orca.core.feed.profile.type.editable.EditableProfile
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.sample.feed.profile.type.editable.createSample
import br.com.orcinus.orca.core.sample.test.feed.profile.post.content.highlight.sample
import br.com.orcinus.orca.core.sample.test.feed.profile.sample
import br.com.orcinus.orca.core.sample.test.image.NoOpSampleImageLoader
import br.com.orcinus.orca.core.sample.test.instance.sample

/** [EditableProfile] returned by [sample]. */
private val testSampleEditableProfile =
  EditableProfile.createSample(
    Instance.sample.profileWriter,
    Instance.sample.postProvider,
    NoOpSampleImageLoader.Provider
  )

/** Test sample [EditableProfile]. */
val EditableProfile.Companion.sample
  get() = testSampleEditableProfile
