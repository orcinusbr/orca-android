package com.jeanbarrossilva.mastodonte.app

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.plusAssign
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.jeanbarrossilva.mastodonte.app.feature.NavGraphs
import com.jeanbarrossilva.mastodonte.app.feature.destinations.ProfileDestination
import com.jeanbarrossilva.mastodonte.app.feature.profile.ProfileModule
import com.jeanbarrossilva.mastodonte.app.feature.tootdetails.TootDetailsModule
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.scope.DestinationScopeWithNoDependencies
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

@Composable
@OptIn(
    ExperimentalMaterialNavigationApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class
)
internal fun Mastodonte(modifier: Modifier = Modifier) {
    val engine = rememberAnimatedNavHostEngine(
        rootDefaultAnimations = RootNavGraphDefaultAnimations(
            enterTransition = {
                fadeIn() + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            },
            exitTransition = {
                fadeOut() + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            },
            popEnterTransition = {
                fadeIn() + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            },
            popExitTransition = {
                fadeOut() + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            }
        )
    )
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController =
        engine.rememberNavController().apply { navigatorProvider += bottomSheetNavigator }
    val destination by navController.currentDestinationAsState()
    val onBottomAreaAvailabilityChangeListener =
        remember(::MastodonteOnBottomAreaAvailabilityChangeListener)
    val bottomBarTonalElevation by animateDpAsState(
        if (onBottomAreaAvailabilityChangeListener.isAvailable) {
            BottomAppBarDefaults.ContainerElevation
        } else {
            0.dp
        }
    )

    MastodonteTheme {
        ModalBottomSheetLayout(
            bottomSheetNavigator,
            sheetShape = BottomSheetDefaults.ExpandedShape
        ) {
            @Suppress("UnusedMaterial3ScaffoldPaddingParameter")
            Scaffold(
                modifier,
                bottomBar = {
                    Column {
                        Divider()

                        BottomAppBar(tonalElevation = bottomBarTonalElevation) {
                            NavigationBarItem(
                                selected = destination in ProfileDestination,
                                onClick = { navController.navigate(ProfileDestination) },
                                icon = {
                                    Icon(
                                        MastodonteTheme.Icons.AccountCircle,
                                        contentDescription = "Profile"
                                    )
                                }
                            )
                        }
                    }
                },
                containerColor = MastodonteTheme.colorScheme.background,
                contentWindowInsets = ScaffoldDefaults
                    .contentWindowInsets
                    .only(WindowInsetsSides.Start)
                    .only(WindowInsetsSides.End)
                    .only(WindowInsetsSides.Bottom)
            ) {
                DestinationsNavHost(
                    NavGraphs.root,
                    Modifier.padding(it),
                    engine = engine,
                    navController = navController,
                    dependenciesContainerBuilder = {
                        injectNavigator()
                        injectFeatureModules()
                        dependency(onBottomAreaAvailabilityChangeListener)
                    }
                )
            }
        }
    }
}

private fun DestinationScopeWithNoDependencies<*>.injectNavigator() {
    val module = module {
        single {
            destinationsNavigator
        }
    }
    loadKoinModules(module)
}

private fun injectFeatureModules() {
    val modules = listOf(ProfileModule(), TootDetailsModule())
    loadKoinModules(modules)
}

@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun MastodontePreview() {
    Mastodonte()
}
