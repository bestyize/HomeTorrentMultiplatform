package com.home.torrent.main.page.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavDeepLink
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.home.torrent.main.page.main.MainPage
import com.home.torrent.main.page.splash.page.SplashPage
import com.thewind.widget.animation.slideInFromRight
import com.thewind.widget.animation.slideOutToRight
import com.thewind.widget.nav.LocalMainNavigation

@Composable
fun MainNavigationRouter() {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalMainNavigation provides navController) {
        NavHost(navController = navController, startDestination = Router.PAGE_SPLASH) {
            composable(route = Router.PAGE_SPLASH) {
                SplashPage {
                    navController.popBackStack()
                    navController.navigate(Router.PAGE_MAIN) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
            composable(route = Router.PAGE_MAIN) {
                MainPage()
            }
            composable(
                route = Router.PAGE_SETTING,
                deepLinks = listOf(NavDeepLink(Router.PAGE_SETTING)),
                enterTransition = slideInFromRight,
                exitTransition = slideOutToRight
            ) {
                //MainSettingPage()
            }

            composable(
                route = Router.PAGE_RECOMMEND_DETAIL,
                arguments = listOf(navArgument("postId") { defaultValue = 0L }),
                enterTransition = slideInFromRight,
                exitTransition = slideOutToRight
            ) {
                val posterId = it.arguments?.getLong("postId") ?: 0L
                //PosterDetailPage(posterId = posterId)
            }

        }
    }

}