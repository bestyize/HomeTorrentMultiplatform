package xyz.thewind.shortvideo.vm

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import xyz.thewind.shortvideo.model.ShortVideoItem
import xyz.thewind.shortvideo.model.ShortVideoPageState

class ShortVideoPageViewModel : ScreenModel {
    private val _state: MutableStateFlow<ShortVideoPageState> = MutableStateFlow(ShortVideoPageState())

    val state = _state.asStateFlow()

    init {
        addVideo()
    }

    fun addVideo() {
        val data = _state.value
        _state.value = data.copy(videos = listOf(
            ShortVideoItem("https://cdn.pixabay.com/video/2023/10/22/186115-877653483_large.mp4"),
            ShortVideoItem("https://cdn.pixabay.com/video/2024/06/06/215472_large.mp4"),
            ShortVideoItem("https://cdn.pixabay.com/video/2024/07/27/223461_large.mp4"),
            ShortVideoItem("https://pixabay.com/videos/rock-stone-sunset-water-calm-217850/"),
        ))
    }
}