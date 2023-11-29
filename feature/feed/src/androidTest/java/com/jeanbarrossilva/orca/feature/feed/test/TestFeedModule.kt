package com.jeanbarrossilva.orca.feature.feed.test

import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.test.instance.sample
import com.jeanbarrossilva.orca.feature.feed.FeedModule
import com.jeanbarrossilva.orca.platform.autos.reactivity.OnBottomAreaAvailabilityChangeListener

internal object TestFeedModule :
  FeedModule(
    { Instance.sample.feedProvider },
    { Instance.sample.postProvider },
    { TestFeedBoundary() },
    { OnBottomAreaAvailabilityChangeListener.empty }
  )
