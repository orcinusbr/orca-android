package com.jeanbarrossilva.orca.app.module.core

import android.content.Context
import com.jeanbarrossilva.orca.core.http.HttpBridge
import com.jeanbarrossilva.orca.core.http.get
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.instance.SomeInstance
import com.jeanbarrossilva.orca.core.mastodon.social.instance.MastodonSocialInstance
import com.jeanbarrossilva.orca.core.sharedpreferences.actor.SharedPreferencesActorProvider
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.dsl.binds
import org.koin.dsl.module

@Suppress("FunctionName")
internal fun MainCoreModule(): Module {
    val context = get<Context>()
    val actorProvider = SharedPreferencesActorProvider(context)
    val instance = MastodonSocialInstance(context, actorProvider)
    HttpBridge.cross(instance)
    return CoreModule { instance }
}

@Suppress("FunctionName")
internal inline fun <reified T : SomeInstance> CoreModule(noinline instance: Definition<T>):
    Module {
    return module {
        single(definition = instance).binds(arrayOf(Instance::class, T::class))
    }
}
