package com.home.torrent.collect.model

import com.home.torrent.model.TorrentInfo
import com.thewind.widget.ui.list.lazy.PageLoadState

internal data class TorrentLocalCollectPageUiState(
    val torrentList: List<TorrentInfo> = listOf(),
    val dialogUiState: CollectPageDialogState = CollectPageDialogState(),
    val editDialogState: TorrentLocalCollectDialogUiState = TorrentLocalCollectDialogUiState(),
    val pageLoadState: PageLoadState = PageLoadState.INIT
)
