package com.home.torrent.app

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import com.home.torrent.main.page.main.MainScreen
import com.home.torrent.main.page.splash.page.SplashScreen
import com.thewind.widget.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        Navigator(
            screens = listOf(MainScreen(), SplashScreen()),
            disposeBehavior = NavigatorDisposeBehavior(disposeSteps = true)
        )
    }
}