package com.jeanbarrossilva.orca.platform.ui.core

import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.std.injector.Injector

/** [Instance] provided by the injected [InstanceProvider]. **/
val Instance.Companion.injected
    get() = Injector.get<InstanceProvider>().provide()
