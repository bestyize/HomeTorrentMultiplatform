package com.home.torrent.search.model

import androidx.compose.runtime.Immutable
import com.home.torrent.model.TorrentSource

@Immutable
internal data class TorrentSearchPageState(
    val keyword: String = "",
    val source: List<TorrentSource> = listOf(),
    val tabs: List<TorrentSearchTabState> = listOf(),
    val dialogState: SearchPageDialogState = SearchPageDialogState()
)