package com.thewind.kmmplayer.vlc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import com.thewind.kmmplayer.model.VideoPlayItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter
import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer
import java.awt.Component
import java.util.*
import kotlin.math.roundToInt

@Composable
internal fun VlcPlayer(
    playerState: VideoPlayItem, modifier: Modifier, onFinish: (() -> Unit)?
) {
    val mediaPlayerComponent = remember { initializeMediaPlayerComponent() }
    val mediaPlayer = remember { mediaPlayerComponent.mediaPlayer() }
    mediaPlayer.observerProgress(playerState.onUpdate)
    mediaPlayer.setupVideoFinishHandler(onFinish)

    val factory =
        remember { { mediaPlayerComponent } }/* OR the following code and using SwingPanel(factory = { factory }, ...) */
    // val factory by rememberUpdatedState(mediaPlayerComponent)

    LaunchedEffect(playerState.url) { mediaPlayer.media().play/*OR .start*/(playerState.url) }
    LaunchedEffect(playerState.seek) { mediaPlayer.controls().setPosition(playerState.seek) }
    LaunchedEffect(playerState.speed) { mediaPlayer.controls().setRate(playerState.speed) }
    LaunchedEffect(playerState.volume) { mediaPlayer.audio().setVolume(playerState.volume.toPercentage()) }
    LaunchedEffect(playerState.isResumed) { mediaPlayer.controls().setPause(!playerState.isResumed) }
    LaunchedEffect(playerState.isFullscreen) {
        if (mediaPlayer is EmbeddedMediaPlayer) {/*
             * To be able to access window in the commented code below,
             * extend the player composable function from WindowScope.
             * See https://github.com/JetBrains/compose-jb/issues/176#issuecomment-812514936
             * and its subsequent comments.
             *
             * We could also just fullscreen the whole window:
             * `window.placement = WindowPlacement.Fullscreen`
             * See https://github.com/JetBrains/compose-multiplatform/issues/1489
             */
            // mediaPlayer.fullScreen().strategy(ExclusiveModeFullScreenStrategy(window))
            mediaPlayer.fullScreen().toggle()
        }
    }
    DisposableEffect(Unit) { onDispose(mediaPlayer::release) }
    SwingPanel(
        factory = factory, background = Color.Transparent, modifier = modifier
    )

}

private fun Float.toPercentage(): Int = (this * 100).roundToInt()

/**
 * See https://github.com/caprica/vlcj/issues/887#issuecomment-503288294
 * for why we're using CallbackMediaPlayerComponent for macOS.
 */
private fun initializeMediaPlayerComponent(): Component {
    NativeDiscovery().discover()
    return if (isMacOS()) {
        CallbackMediaPlayerComponent()
    } else {
        EmbeddedMediaPlayerComponent()
    }
}

/**
 * We play the video again on finish (so the player is kind of idempotent),
 * unless the [onFinish] callback stops the playback.
 * Using `mediaPlayer.controls().repeat = true` did not work as expected.
 */
@Composable
private fun MediaPlayer.setupVideoFinishHandler(onFinish: (() -> Unit)?) {
    DisposableEffect(onFinish) {
        val listener = object : MediaPlayerEventAdapter() {
            override fun finished(mediaPlayer: MediaPlayer) {
                onFinish?.invoke()
                mediaPlayer.submit { mediaPlayer.controls().play() }
            }
        }
        events().addMediaPlayerEventListener(listener)
        onDispose { events().removeMediaPlayerEventListener(listener) }
    }
}

/**
 * Checks for and emits video progress every 50 milliseconds.
 * Note that it seems vlcj updates the progress only every 250 milliseconds or so.
 *
 * Instead of using `Unit` as the `key1` for [LaunchedEffect],
 * we could use `media().info()?.mrl()` if it's needed to re-launch
 * the effect (for whatever reason) when the url (aka video) changes.
 */
@Composable
private fun MediaPlayer.observerProgress(onUpdate: (progress: Float, position: Long) -> Unit) {
    LaunchedEffect(key1 = Unit) {
        while (isActive) {
            val progress = status().position()
            val position = status().time()
            onUpdate.invoke(progress, position)
            delay(50)
        }
    }
}

/**
 * Returns [MediaPlayer] from player components.
 * The method names are the same, but they don't share the same parent/interface.
 * That's why we need this method.
 */
private fun Component.mediaPlayer() = when (this) {
    is CallbackMediaPlayerComponent -> mediaPlayer()
    is EmbeddedMediaPlayerComponent -> mediaPlayer()
    else -> error("mediaPlayer() can only be called on vlcj player components")
}

private fun isMacOS(): Boolean {
    val os = System.getProperty("os.name", "generic").lowercase(Locale.ENGLISH)
    return "mac" in os || "darwin" in os
}