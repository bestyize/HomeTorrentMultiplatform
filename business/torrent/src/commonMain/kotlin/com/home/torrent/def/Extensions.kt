package com.home.torrent.def

import io.ktor.http.*


internal val String.urlEncode: String
    get() = encodeURLPath()

internal val String?.magnetUrlToHash: String
    get() {
        this ?: return ""
        if (!startsWith(KEY_MAGNET_PREFIX)) return ""
        val lastIndex = if (this.contains("&")) indexOf("&") else length
        return substring(KEY_MAGNET_PREFIX.length, lastIndex).uppercase()
    }

internal val String?.magnetUrlToTorrentUrl: String
    get() {
        this ?: return ""
        if (isEmpty()) return ""
        return COMMON_TORREND_FILE_URL.replace(KEY_REPLACE_HASH, magnetUrlToHash)
    }

internal val String?.hashToTorrentUrl: String
    get() {
        this ?: return ""
        if (isEmpty()) return ""
        return COMMON_TORREND_FILE_URL.replace(KEY_REPLACE_HASH, uppercase())
    }

internal val String?.hashToMagnetUrl: String
    get() {
        this ?: return ""
        return KEY_MAGNET_PREFIX + this
    }