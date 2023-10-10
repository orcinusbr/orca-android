package com.jeanbarrossilva.orca.app.module.feature.search

import com.jeanbarrossilva.orca.core.http.HttpModule
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.feature.search.SearchModule
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.std.injector.Injector

internal class MainSearchModule(navigator: Navigator) : SearchModule(
    { Injector.from<HttpModule>().get<InstanceProvider>().provide().profileSearcher },
    { NavigatorSearchBoundary(navigator) }
)
