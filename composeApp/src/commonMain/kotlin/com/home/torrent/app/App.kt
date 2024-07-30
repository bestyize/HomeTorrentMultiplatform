package com.home.torrent.app

import androidx.compose.runtime.Composable
import com.home.torrent.main.page.main.MainPage
import com.home.torrent.main.page.nav.MainNavigationRouter
import com.thewind.widget.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        MainNavigationRouter()
        //MainPage()
    }
}