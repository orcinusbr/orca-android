package com.jeanbarrossilva.orca.core.sample.instance

import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.core.instance.SomeInstance

/** [InstanceProvider] returned by [sample]. */
private val sampleInstanceProvider =
  object : InstanceProvider {
    override fun provide(): SomeInstance {
      return Instance.sample
    }
  }

/** Sample [InstanceProvider]. */
val InstanceProvider.Companion.sample
  get() = sampleInstanceProvider
