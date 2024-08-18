package com.home.torrent.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TorrentInfo(
    @SerialName("hash") val hash: String? = null,
    @SerialName("detail_url") val detailUrl: String? = null,
    @SerialName("magnet_url") val magnetUrl: String? = null,
    @SerialName("torrent_url") val torrentUrl: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("date") val date: String? = null,
    @SerialName("last_download_date") val lastDownloadDate: String? = null,
    @SerialName("size") val size: String? = null,
    @SerialName("src") val src: Int = 0,
    @SerialName("download_count") val downloadCount: String? = null,
    @SerialName("leacher_count") val leacherCount: String? = null,
    @SerialName("sender_count") val senderCount: String? = null
)