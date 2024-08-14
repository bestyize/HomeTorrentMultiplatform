package com.thewind.kmmplayer.platform

data class PlayerState(
    val isSurfaceAvailable: Boolean = false,
    val prepared: Boolean = false,
    val isPlaying: Boolean = false
)
