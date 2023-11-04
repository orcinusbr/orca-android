package com.jeanbarrossilva.orca.feature.feed.test

import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.test.instance.sample
import com.jeanbarrossilva.orca.feature.feed.FeedModule
import com.jeanbarrossilva.orca.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener

internal object TestFeedModule :
  FeedModule(
    { Instance.sample.feedProvider },
    { Instance.sample.tootProvider },
    { TestFeedBoundary() },
    { OnBottomAreaAvailabilityChangeListener.empty }
  )
