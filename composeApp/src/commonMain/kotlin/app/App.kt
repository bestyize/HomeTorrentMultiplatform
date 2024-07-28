package app

import androidx.compose.runtime.Composable
import app.home.homepage.HomePage
import framework.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        HomePage()
    }
}