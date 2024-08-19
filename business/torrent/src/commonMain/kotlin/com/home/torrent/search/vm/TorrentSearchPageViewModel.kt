package com.home.torrent.search.vm

import cafe.adriel.voyager.core.model.ScreenModel
import com.home.torrent.collect.service.TorrentCollectService
import com.home.torrent.model.TorrentInfo
import com.home.torrent.model.TorrentSource
import com.home.torrent.search.model.SearchPageDialogState
import com.home.torrent.search.model.SearchPageDialogType
import com.home.torrent.search.model.TorrentSearchPageState
import com.home.torrent.search.model.TorrentSearchTabState
import com.home.torrentcenter.services.requestTorrentSources
import com.home.torrentcenter.services.suspendSearchMagnetUrl
import com.home.torrentcenter.services.suspendSearchTorrent
import com.home.torrentcenter.services.suspendSearchTorrentUrl
import com.home.torrentcenter.services.transferMagnetUrlToHash
import com.home.torrentcenter.services.transferMagnetUrlToTorrentUrl
import com.thewind.kmmkv.KmmKv
import com.thewind.widget.ui.list.lazy.PageLoadState
import com.thewind.widget.ui.toast.toast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * @author: read
 * @date: 2023/9/12 上午1:42
 * @description:
 */
internal class TorrentSearchPageViewModel : ScreenModel {


    private val loadingSet = mutableSetOf<Int>()

    private val _searchPageState: MutableStateFlow<TorrentSearchPageState> = MutableStateFlow(
        TorrentSearchPageState(source = loadTorrentSourcesLocal())
    )

    val searchPageState = _searchPageState.asStateFlow()

    init {
        val isFinish =
            _searchPageState.value.source.isNotEmpty() && _searchPageState.value.source.size == _searchPageState.value.tabs.size
        if (!isFinish) {
            val data = _searchPageState.value
            _searchPageState.value = data.copy(tabs = data.source.map {
                TorrentSearchTabState(src = it.src)
            })
        }
    }

    fun forceReloadTabKeyword(tabIndex: Int) {
        val data = searchPageState.value
        val tabData = data.tabs.getOrNull(tabIndex) ?: return
        if (loadingSet.contains(tabIndex)) {
            return
        }
        _searchPageState.value = data.copy(tabs = data.tabs.toMutableList().apply {
            this[tabIndex] = TorrentSearchTabState(src = tabData.src, keyword = data.keyword)
        })
    }

    fun reloadTabKeywordWhenPageSwitch(tabIndex: Int) {
        val data = searchPageState.value
        val tabData = data.tabs.getOrNull(tabIndex) ?: return
        if (tabData.keyword != data.keyword) {
            forceReloadTabKeyword(tabIndex)
        }
    }

    suspend fun loadMore(src: Int, pageIndex: Int) {
        val data = _searchPageState.value
        val tabStates = _searchPageState.value.tabs
        val tabState = tabStates.find { it.src == src } ?: return
        if (loadingSet.contains(pageIndex)) return
        when (tabState.loadState) {
            PageLoadState.INIT -> {
                loadingSet.add(pageIndex)
                val list = suspendSearchTorrent(
                    src = src, key = _searchPageState.value.keyword, page = tabState.page
                )
                _searchPageState.value = data.copy(tabs = data.tabs.toMutableList().apply {
                    forEachIndexed { index, tabData ->
                        if (tabData.src == src) {
                            this[index] = tabData.copy(
                                page = if (list.isEmpty()) 1 else 2,
                                keyword = data.keyword,
                                dataList = list,
                                loadState = if (list.isEmpty()) PageLoadState.ALL_LOADED else PageLoadState.FINISH
                            )
                        }
                    }
                })
                loadingSet.remove(pageIndex)
            }

            PageLoadState.FINISH -> {
                loadingSet.add(pageIndex)
                val list = suspendSearchTorrent(
                    src = src, key = _searchPageState.value.keyword, page = tabState.page
                )
                _searchPageState.value = data.copy(tabs = data.tabs.toMutableList().apply {
                    forEachIndexed { index, tabData ->
                        if (tabData.src == src) {
                            this[index] = tabData.copy(
                                page = if (list.isEmpty()) tabData.page else tabData.page + 1,
                                keyword = data.keyword,
                                dataList = tabData.dataList.toMutableList().apply {
                                    addAll(list)
                                },
                                loadState = if (list.isEmpty()) PageLoadState.ALL_LOADED else PageLoadState.FINISH
                            )
                        }
                    }
                })
                loadingSet.remove(pageIndex)
            }

            PageLoadState.ALL_LOADED -> {

            }

            PageLoadState.ERROR -> {

            }
        }


    }

    fun updateKeyword(keyword: String) {
        _searchPageState.value = _searchPageState.value.copy(keyword = keyword)
    }

    fun handleTorrentInfoClick(data: TorrentInfo) {
        val stateData = _searchPageState.value
        _searchPageState.value = stateData.copy(
            dialogState = stateData.dialogState.copy(
                type = SearchPageDialogType.OPTION, data = data
            )
        )

    }

    suspend fun updateDialogState(
        data: TorrentInfo? = _searchPageState.value.dialogState.data, isMagnet: Boolean = true
    ) {
        if (data == null || data.detailUrl.isNullOrBlank()) {
            _searchPageState.value =
                _searchPageState.value.copy(dialogState = SearchPageDialogState())
            return
        }
        val dialogState = _searchPageState.value.dialogState
        _searchPageState.value = _searchPageState.value.copy(
            dialogState = dialogState.copy(
                type = SearchPageDialogType.ADDRESS, data = data.copy(
                    magnetUrl = when {
                        !data.magnetUrl.isNullOrBlank() -> data.magnetUrl
                        else -> suspendSearchMagnetUrl(
                            data.src, data.detailUrl
                        )
                    }, torrentUrl = when {
                        !data.torrentUrl.isNullOrBlank() -> data.torrentUrl
                        !data.magnetUrl.isNullOrBlank() -> transferMagnetUrlToTorrentUrl(data.magnetUrl)
                        else -> suspendSearchTorrentUrl(data.src, data.detailUrl)
                    }
                ), isMagnet = isMagnet
            )
        )

    }

    suspend fun collectToCloud(torrent: TorrentInfo?) {
        torrent ?: return
        val magnetUrl = if (torrent.magnetUrl.isNullOrBlank()) suspendSearchMagnetUrl(
            torrent.src, torrent.detailUrl!!
        ) else torrent.magnetUrl
        if (magnetUrl == null) {
            toast("收藏到云端失败！")
            return
        }
        val hash =
            if (torrent.hash.isNullOrBlank()) transferMagnetUrlToHash(magnetUrl) else torrent.hash
        val dat = torrent.copy(
            magnetUrl = magnetUrl,
            hash = if (hash.isBlank()) magnetUrl else hash,
            torrentUrl = if (torrent.torrentUrl.isNullOrBlank()) transferMagnetUrlToTorrentUrl(
                magnetUrl
            ) else torrent.torrentUrl
        )
        toast(TorrentCollectService.collectToCloud(dat).message)
        updateDialogState(null)
    }

}


internal fun loadTorrentSourcesLocal(): List<TorrentSource> {
    runCatching {
        val localData = KmmKv.defaultKmmKv().decode(SP_LOCAL_TORRENT_SOURCES, null) ?: ""
        val list: List<TorrentSource>? = Json.decodeFromString<List<TorrentSource>?>(localData)
        if (!list.isNullOrEmpty()) return list
    }
    return requestTorrentSources()
}

private fun saveTorrentSourcesToLocal(data: List<TorrentSource>?) {
    if (data.isNullOrEmpty()) return
    KmmKv.defaultKmmKv().encode(SP_LOCAL_TORRENT_SOURCES, Json.encodeToString(data))
}

internal const val SP_LOCAL_TORRENT_SOURCES = "sp_local_torrent_sources"

private const val TAG = "[Torrent]TorrentSearchPageViewModel"