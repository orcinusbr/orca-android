package com.jeanbarrossilva.orca.core.sharedpreferences.feed.profile.toot.muting

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.jeanbarrossilva.orca.core.feed.profile.toot.muting.TermMuter
import kotlinx.coroutines.flow.MutableStateFlow

/** Provides and controls configurations of the [SharedPreferences]-specific [TermMuter]. **/
internal object SharedPreferencesTermMuter {
    /**
     * Gets the [SharedPreferences] related to the [SharedPreferences]-specific [TermMuter].
     *
     * @param context [Context] through which the [SharedPreferences] will be obtained.
     **/
    fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("shared-preferences-term-muter", Context.MODE_PRIVATE)
    }

    /**
     * Clears all persisted terms.
     *
     * @param context [Context] through which the [SharedPreferences] will be obtained.
     **/
    fun reset(context: Context) {
        getPreferences(context).edit(action = SharedPreferences.Editor::clear)
    }
}

/**
 * [TermMuter] that persists muted terms through the [SharedPreferences] API.
 *
 * @param context [Context] through which the [SharedPreferences] will be created.
 **/
@Suppress("FunctionName")
fun SharedPreferencesTermMuter(context: Context): TermMuter {
    val preferences = SharedPreferencesTermMuter.getPreferences(context)
    val termsFlow = MutableStateFlow(emptyList<String>())
    return TermMuter {
        getTerms { termsFlow }
        mute {
            preferences.edit { putString(it, it) }
            termsFlow.value += it
        }
        unmute {
            preferences.edit { remove(it) }
            termsFlow.value -= it
        }
    }
}
