package com.jeanbarrossilva.orca.core.sample.test.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.feed.profile.createSample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.toot.content.highlight.sample
import com.jeanbarrossilva.orca.core.sample.test.image.TestSampleImageLoader
import com.jeanbarrossilva.orca.core.sample.test.instance.sample

/** [Profile] returned by [sample]. */
private val testSampleProfile =
  Profile.createSample(Instance.sample.tootProvider, TestSampleImageLoader.Provider)

/** Test sample [Profile]. */
val Profile.Companion.sample
  get() = testSampleProfile
