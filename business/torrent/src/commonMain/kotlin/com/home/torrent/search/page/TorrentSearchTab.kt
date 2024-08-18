package com.home.torrent.search.page

import androidx.compose.runtime.Composable
import com.home.torrent.model.TorrentInfo
import com.home.torrent.search.model.TorrentSearchTabState
import com.home.torrent.widget.TorrentListView

/**
 * @author: read
 * @date: 2023/9/12 上午1:29
 * @description:
 */

@Composable
internal fun TorrentSearchTab(
    tabState: TorrentSearchTabState,
    onLoad: () -> Unit,
    collectSet: Set<TorrentInfo>,
    onCollect: (TorrentInfo, Boolean) -> Unit,
    onClick: (TorrentInfo) -> Unit
) {
    TorrentListView(
        list = tabState.dataList,
        collectSet = collectSet,
        pageLoadState = tabState.loadState,
        onClick = onClick,
        onCollect = onCollect,
        onLoad = onLoad
    )
}
