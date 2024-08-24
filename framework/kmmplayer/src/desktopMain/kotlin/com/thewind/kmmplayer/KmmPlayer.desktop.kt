package com.thewind.kmmplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.thewind.kmmplayer.vlc.VlcPlayer
import com.thewind.kmmplayer.vm.PlayerViewModel

@Composable
actual fun KmmPlayer(
    modifier: Modifier,
    url: String,
) {
    val vm = remember { PlayerViewModel.INSTANCE }

    val playerState by vm.playerState.collectAsState()

    vm.addPlayItem(url)

    playerState.currentItem?.let { item ->
        VlcPlayer(item, modifier = Modifier.fillMaxSize().background(color = Color.Black), onFinish = {

        })
    }


}