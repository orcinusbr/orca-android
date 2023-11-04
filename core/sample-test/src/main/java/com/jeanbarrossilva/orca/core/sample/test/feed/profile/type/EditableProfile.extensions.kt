package com.jeanbarrossilva.orca.core.sample.test.feed.profile.type

import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.feed.profile.type.editable.createSample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.sample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.toot.content.highlight.sample
import com.jeanbarrossilva.orca.core.sample.test.image.TestSampleImageLoader
import com.jeanbarrossilva.orca.core.sample.test.instance.sample

/** [EditableProfile] returned by [sample]. */
private val testSampleEditableProfile =
  EditableProfile.createSample(
    Instance.sample.profileWriter,
    Instance.sample.tootProvider,
    TestSampleImageLoader.Provider
  )

/** Test sample [EditableProfile]. */
val EditableProfile.Companion.sample
  get() = testSampleEditableProfile
