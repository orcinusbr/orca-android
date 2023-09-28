package com.jeanbarrossilva.orca.platform.ui.core

import android.content.ComponentCallbacks
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.core.instance.SomeInstance
import org.koin.android.ext.android.get

/** Gets the [Instance] provided by the injected [InstanceProvider]. **/
fun ComponentCallbacks.instance(): SomeInstance {
    return get<InstanceProvider>().provide()
}
