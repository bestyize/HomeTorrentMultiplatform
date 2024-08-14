package com.thewind.kmmplayer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.thewind.kmmplayer.platform.PlayerView

@Composable
actual fun KmmPlayer(modifier: Modifier, url: String) {
    AndroidView(modifier = modifier, factory = { context ->
        PlayerView(context).apply {
            play(url = url)
        }
    })
}