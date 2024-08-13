package com.home.torrent.main.page.main.vm

import cafe.adriel.voyager.core.model.ScreenModel
import com.home.torrent.main.page.main.model.HomePageState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ScreenModel {
    private val _state: MutableStateFlow<HomePageState> = MutableStateFlow(HomePageState())

    val state = _state.asStateFlow()

}