package com.home.torrent.collect.model

internal data class TorrentCollectListResponse(
    val code: Int = 0, val message: String? = null, val data: List<TorrentInfoBean>? = null
)