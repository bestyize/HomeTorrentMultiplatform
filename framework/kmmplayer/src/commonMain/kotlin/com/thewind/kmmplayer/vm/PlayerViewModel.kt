package com.thewind.kmmplayer.vm

import com.thewind.kmmplayer.model.VideoPlayItem
import com.thewind.kmmplayer.model.VideoPlayerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayerViewModel {
    private val _playerState: MutableStateFlow<VideoPlayerState> = MutableStateFlow(VideoPlayerState())

    val playerState = _playerState.asStateFlow()


    fun updateProgress(playId: String, progress: Float, position: Long) {
        val data = _playerState.value
        _playerState.value = data.copy(playList = data.playList.toMutableList().apply {
            forEachIndexed { index, item ->
                if (item.playId == playId) {
                    this[index] = item.copy(progress = progress, position = position)
                }
            }
        })
    }

    fun pause(playId: String) {
        val data = _playerState.value
        _playerState.value = data.copy(playList = data.playList.toMutableList().apply {
            forEachIndexed { index, item ->
                if (item.playId == playId) {
                    this[index] = item.copy(isResumed = false)
                }
            }
        })
    }

    fun play(playId: String) {
        val data = _playerState.value
        _playerState.value = data.copy(playList = data.playList.toMutableList().apply {
            forEachIndexed { index, item ->
                if (item.playId == playId) {
                    this[index] = item.copy(isResumed = true)
                }
            }
        })
    }

    fun addPlayItem(url: String) {
        val data = _playerState.value
        val targetItem = data.playList.find { it.playId == url }
        _playerState.value = if (targetItem == null) {
            val item = VideoPlayItem(playId = url, url = url, onUpdate = { progress, position ->
                updateProgress(url, progress, position)
            })
            data.copy(currentItem = item, playList = data.playList.toMutableList().apply {
                add(
                    element = item
                )
            })
        } else {
            data.copy(currentItem = targetItem)
        }


    }

    companion object {
        val INSTANCE by lazy { PlayerViewModel() }
    }
}