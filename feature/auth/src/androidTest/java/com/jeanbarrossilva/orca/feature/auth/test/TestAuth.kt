package com.jeanbarrossilva.orca.feature.auth.test

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.orca.feature.auth.Auth

@Composable
@Suppress("TestFunctionName")
internal fun TestAuth(modifier: Modifier = Modifier) {
    var username by remember { mutableStateOf("") }
    var instance by remember { mutableStateOf("") }

    Auth(
        username,
        onUsernameChange = { username = it },
        instance,
        onDomainChange = { instance = it },
        onSignIn = { },
        modifier
    )
}
