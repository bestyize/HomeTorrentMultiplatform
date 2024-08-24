package com.thewind.kmmplayer.model

data class VideoPlayItem(
    val playId: String = "",
    val url: String = "",
    val seek: Float = 0f,
    val speed: Float = 1f,
    val volume: Float = 1f,
    val isResumed: Boolean = true,
    val isFullscreen: Boolean = true,
    val position: Long = 0L,
    val progress: Float = 0f,
    val onUpdate: (progress: Float, position: Long) -> Unit = { _, _ -> },
)