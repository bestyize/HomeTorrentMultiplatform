package com.thewind.kmmplayer.model

data class VideoPlayerState(
    val playList: List<VideoPlayItem> = emptyList(),
    val currentItem: VideoPlayItem? = null,
)