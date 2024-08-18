package com.home.torrentcenter.services

import com.home.torrent.def.TorrentSrc
import com.home.torrent.def.magnetUrlToHash
import com.home.torrent.def.magnetUrlToTorrentUrl
import com.home.torrent.model.TorrentInfo
import com.home.torrent.model.TorrentSource
import com.thewind.network.HttpUtil.get
import com.thewind.network.appHost
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json


suspend fun searchTorrent(src: Int, key: String, page: Int = 1): List<TorrentInfo> {
    runCatching {
        val resp = get("$appHost/torrent/api/search?src=$src&key=${key.encodeURLPathPart()}&page=$page")
        val list: List<TorrentInfo> = Json.decodeFromString<List<TorrentInfo>>(resp)
        return list ?: emptyList()
    }.onFailure {
        it.printStackTrace()
        println(it)
    }

    return emptyList()


}


suspend fun suspendSearchTorrent(src: Int, key: String, page: Int = 1) = withContext(Dispatchers.IO) {
    searchTorrent(src, key, page)
}

suspend fun searchMagnetUrl(src: Int, detailUrl: String): String {
    runCatching {
        return get("$appHost/torrent/api/magnet/url?src=$src&detailUrl=${detailUrl.encodeURLPathPart()}")
    }
    return ""
}

suspend fun suspendSearchMagnetUrl(src: Int, detailUrl: String) = withContext(Dispatchers.IO) {
    searchMagnetUrl(src, detailUrl)
}

suspend fun searchTorrentUrl(src: Int, detailUrl: String): String {
    runCatching {
        return get("$appHost/torrent/api/torrent/url?src=$src&detailUrl=${detailUrl.encodeURLPathPart()}")
    }
    return ""
}

suspend fun suspendSearchTorrentUrl(src: Int, detailUrl: String) = withContext(Dispatchers.IO) {
    searchTorrentUrl(src, detailUrl)
}

fun transferMagnetUrlToTorrentUrl(magnetUrl: String): String {
    if (magnetUrl.isEmpty()) return ""
    if (magnetUrl.endsWith(".torrent")) return magnetUrl
    return magnetUrl.magnetUrlToTorrentUrl
}

fun transferMagnetUrlToHash(magnetUrl: String) = magnetUrl.magnetUrlToHash

fun requestTorrentSources(): List<TorrentSource> = getSupportTorrentSources()

internal fun getSupportTorrentSources(): List<TorrentSource> = listOf(
    TorrentSource(
        src = TorrentSrc.SOLID_TORRENT.ordinal,
        title = "Solid",
        officialUrl = "https://solidtorrent.to"
    ),
    TorrentSource(
        src = TorrentSrc.BT_SOW.ordinal, title = "BtSow", officialUrl = "https://btsow.com"
    ),
    TorrentSource(
        src = TorrentSrc.TORRENT_KITTY.ordinal,
        title = "Kitty",
        officialUrl = "https://www.torrentkitty.tv/"
    ),
    TorrentSource(
        src = TorrentSrc.ZERO_MAGNET.ordinal, title = "Zero", officialUrl = "https://0magnet.com"
    ),
    TorrentSource(
        src = TorrentSrc.X_1337.ordinal, title = "1337", officialUrl = "https://www.1337xx.to"
    ),
    TorrentSource(
        src = TorrentSrc.TOR_LOCK.ordinal,
        title = "TorLock",
        officialUrl = "https://www.torlock.com"
    ),
    TorrentSource(
        src = TorrentSrc.CILIMAO.ordinal,
        title = "磁力猫",
        officialUrl = "https://clmclm.com/"
    ),
    TorrentSource(
        src = TorrentSrc.ACG_RIP.ordinal, title = "AcgRip", officialUrl = "https://acg.rip"
    ),
    TorrentSource(src = TorrentSrc.PIRATE_BAY.ordinal, title = "海盗湾", officialUrl = ""),
)