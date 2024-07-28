import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import app.App
import framework.theme.AppTheme
import framework.theme.LocalColors
import widget.TopAppBar

fun main() = application {
    val windowSize = remember { DpSize(380.dp, 800.dp) }
    val windowState = rememberWindowState(size = windowSize, position = WindowPosition(Alignment.Center))
    Window(
        onCloseRequest = ::exitApplication,
        resizable = true,
        transparent = true,
        undecorated = true,
        alwaysOnTop = true,
        state = windowState
    ) {
        AppTheme {
            Column(
                modifier = Modifier.fillMaxSize().clip(shape = RoundedCornerShape(10.dp))
                    .border(width = 0.5.dp, color = LocalColors.current.Ga4, shape = RoundedCornerShape(10.dp))
            ) {
                WindowDraggableArea(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                    TopAppBar(title = "磁力之家", onMin = {}, backgroundColor = LocalColors.current.Ga4, onClose = {
                        exitApplication()
                    })
                }
                App()
            }
        }
    }
}