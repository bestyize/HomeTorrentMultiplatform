package com.home.torrent.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import com.home.torrent.main.page.main.MainScreen
import com.home.torrent.main.page.splash.page.SplashScreen
import com.thewind.widget.theme.AppTheme
import com.thewind.widget.theme.ThemeId
import com.thewind.widget.theme.ThemeManager
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val theme = ThemeManager.themeFlow.collectAsState(initial = ThemeId.AUTO)
    val isNight = when (theme.value) {
        ThemeId.AUTO -> isSystemInDarkTheme()
        ThemeId.NIGHT -> true
        ThemeId.DAY -> false
    }
    AppTheme(darkTheme = isNight) {
        Navigator(
            screens = listOf(MainScreen(), SplashScreen()),
            disposeBehavior = NavigatorDisposeBehavior(disposeSteps = true)
        )
    }
}